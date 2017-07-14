package main;

import java.security.Principal;
import java.util.LinkedList;
import javax.validation.Valid;
import main.entities.Basvuru;
import main.entities.Ilan;
import main.services.AdayService;
import main.services.BasvuruService;
import main.services.IlanService;
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

    @RequestMapping("/ilanlar/{adayID}")
    public String tumIlanlar(@PathVariable String adayID, Model model) {
        LinkedList<Ilan> ilanlar = new LinkedList<>();
        for (Basvuru basvuru : adayService.adayBul(adayID).getBasvurular()) {
            ilanlar.add(basvuru.getIlan());
        }
        model.addAttribute("ilanlar", ilanlar);
        return "/uzman/diger";
    }

    @RequestMapping("/diger/{basvuruKodu}")
    public String tumIlanlar(@PathVariable Integer basvuruKodu, Model model) {
        Basvuru basvuru = basvuruService.basvuruBul(basvuruKodu);
        LinkedList<Ilan> ilanlar = new LinkedList<>();
        for (Basvuru b : basvuru.getAday().getBasvurular()) {
            ilanlar.add(ilanService.ilanBul(b.getIlan().getKod()));
        }
        ilanlar.remove(ilanService.ilanBul(basvuru.getIlan().getKod()));
        model.addAttribute("ilanlar", ilanlar);
        return "/uzman/diger";
    }

    @RequestMapping("/ilan/{ilanKodu}")
    public String ilanBul(@PathVariable int ilanKodu, Model model) {
        model.addAttribute("ilan", ilanService.ilanBul(ilanKodu));
        return "/uzman/ilan";
    }

    @RequestMapping("/basvurular")
    public String tumBasvurular(Model model, Principal principal) {
        LinkedList<Basvuru> basvurular = new LinkedList<>();
        for (Ilan ilan : ilanService.tumIlanlar(principal.getName())) {
            basvurular.addAll(ilan.getBasvurular());
        }
        model.addAttribute("basvurular", basvurular);
        return "/uzman/basvurular";
    }

    @RequestMapping("/basvurular/{ilanKodu}")
    public String tumBasvurular(@PathVariable int ilanKodu, Model model) {
        model.addAttribute("basvurular", ilanService.ilanBul(ilanKodu).getBasvurular());
        return "/uzman/basvurular";
    }

    @RequestMapping("/basvuru/{basvuruKodu}")
    public String basvuruBul(@PathVariable Integer basvuruKodu, Model model) {
        model.addAttribute("basvuru", basvuruService.basvuruBul(basvuruKodu));
        return "/uzman/basvuru";
    }

    @RequestMapping("/adaylar")
    public String tumAdaylar(Model model) {
        model.addAttribute("adaylar", adayService.tumAdaylar());
        return "/uzman/adaylar";
    }

    @RequestMapping("/aday/{adayKodu}")
    public String adayBul(@PathVariable String adayKodu, Model model) {
        model.addAttribute("aday", adayService.adayBul(adayKodu));
        return "/uzman/aday";
    }

}
