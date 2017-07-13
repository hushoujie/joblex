package main;

import java.security.Principal;
import java.util.LinkedList;
import java.util.TreeMap;
import javax.validation.Valid;
import main.domains.Aday;
import main.domains.Basvuru;
import main.domains.Ilan;
import main.domains.Pozisyon;
import main.services.AdayService;
import main.services.BasvuruService;
import main.services.IlanService;
import main.services.PozisyonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/uzman")
public class UzmanController {

    private AdayService adayService;

    private BasvuruService basvuruService;

    private IlanService ilanService;

    private PozisyonService pozisyonService;

    @Autowired
    public void setAdayService(AdayService adayService) {
        this.adayService = adayService;
    }

    @Autowired
    public void setBasvuruService(BasvuruService basvuruService) {
        this.basvuruService = basvuruService;
    }

    @Autowired
    public void setIlanService(IlanService ilanService) {
        this.ilanService = ilanService;
    }

    @Autowired
    public void setPozisyonService(PozisyonService pozisyonService) {
        this.pozisyonService = pozisyonService;
    }

    @RequestMapping("/")
    public String anaSayfa(Model model) {
        return "/uzman/ana-sayfa";
    }

    @RequestMapping("/ilan/ekle")
    public String ilanEkle(Model model) {
        model.addAttribute("ilan", new Ilan());
        return "/uzman/ilan-kaydet";
    }

    @RequestMapping("/ilan/duzenle/{ilanKodu}")
    public String ilanDuzenle(@PathVariable int ilanKodu, Model model) {
        model.addAttribute("ilan", ilanService.ilanBul(ilanKodu));
        return "/uzman/ilan-kaydet";
    }

    @RequestMapping("/ilan/kaydet")
    public String ilanKaydet(@Valid Ilan ilan, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "/uzman/ilan-kaydet";
        }
        ilan.setUzman(principal.getName());
        ilanService.ilanKaydet(ilan);
        return "redirect:/uzman/ilan/" + ilan.getKod();
    }

    @RequestMapping("/ilan/sil/{ilanKodu}")
    public String ilanSil(@PathVariable int ilanKodu) {
        ilanService.ilanSil(ilanKodu);
        return "redirect:/uzman/ilanlar";
    }

    @RequestMapping("/ilanlar")
    public String tumIlanlar(Model model, Principal principal) {
        model.addAttribute("ilanlar", ilanService.tumIlanlar(principal.getName()));
        return "/uzman/ilanlar";
    }

    @RequestMapping("/ilan/{ilanKodu}")
    public String ilanBul(@PathVariable int ilanKodu, Model model) {
        model.addAttribute("ilan", ilanService.ilanBul(ilanKodu));
        return "/uzman/ilan";
    }

    @RequestMapping("/basvurular")
    public String tumBasvurular(Model model, Principal principal) {
        TreeMap<Basvuru, Aday> basvurular = new TreeMap<>();
        for (Basvuru basvuru : basvuruService.tumBasvurular()) {
            if (ilanService.ilanBul(basvuru.getIlan()).getUzman().equals(principal.getName())) {
                basvurular.put(basvuru, adayService.adayBul(basvuru.getAday()));
            }
        }
        model.addAttribute("basvurular", basvurular);
        return "/uzman/basvurular";
    }

    @RequestMapping("/basvurular/{ilanKodu}")
    public String tumBasvurular(@PathVariable int ilanKodu, Model model) {
        TreeMap<Basvuru, Aday> basvurular = new TreeMap<>();
        for (Basvuru basvuru : basvuruService.tumBasvurular(ilanKodu)) {
            basvurular.put(basvuru, adayService.adayBul(basvuru.getAday()));
        }
        model.addAttribute("basvurular", basvurular);
        return "/uzman/basvurular";
    }

    @RequestMapping("/basvuru/{basvuruKodu}")
    public String basvuruBul(@PathVariable Integer basvuruKodu, Model model) {
        Basvuru basvuru = basvuruService.basvuruBul(basvuruKodu);
        model.addAttribute("basvuru", basvuru);
        model.addAttribute("aday", adayService.adayBul(basvuru.getAday()));
        model.addAttribute("pozisyonlar", pozisyonService.tumPozisyonlar(basvuru.getAday()));
        int i = 0;
        for (Basvuru b : basvuruService.tumBasvurular(basvuru.getAday())) {
            i++;
        }
        model.addAttribute("ilanlar", i - 1);
        return "/uzman/basvuru";
    }

    @RequestMapping("/adaylar")
    public String tumAdaylar(Model model) {
        TreeMap<Aday, Iterable<Pozisyon>> adaylar = new TreeMap<>();
        for (Aday aday : adayService.tumAdaylar()) {
            adaylar.put(aday, pozisyonService.tumPozisyonlar(aday.getId()));
        }
        model.addAttribute("adaylar", adaylar);
        return "/uzman/adaylar";
    }

    @RequestMapping("/aday/{adayKodu}")
    public String adayBul(@PathVariable String adayKodu, Model model) {
        model.addAttribute("aday", adayService.adayBul(adayKodu));
        model.addAttribute("pozisyonlar", pozisyonService.tumPozisyonlar(adayKodu));
        return "/uzman/aday";
    }
    
    @RequestMapping("/diger/{kod}")
    public String digerIlanlar(@PathVariable Integer kod, Model model) {
        Basvuru basvuru = basvuruService.basvuruBul(kod);
        LinkedList<Ilan> ilanlar = new LinkedList<>();
        for (Basvuru b : basvuruService.tumBasvurular(basvuru.getAday())) {
            ilanlar.add(ilanService.ilanBul(b.getIlan()));
        }
        ilanlar.remove(ilanService.ilanBul(basvuru.getIlan()));
        model.addAttribute("ilanlar", ilanlar);
        return "/uzman/diger";
    }

}
