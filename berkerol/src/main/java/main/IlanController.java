package main;

import java.security.Principal;
import javax.validation.Valid;
import main.domains.Ilan;
import main.services.IlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/uzman")
public class IlanController {

    private IlanService ilanService;

    @Autowired
    public void setIlanService(IlanService ilanService) {
        this.ilanService = ilanService;
    }

    @RequestMapping("/ilan/ekle")
    public String ilanEkle(Model model) {
        model.addAttribute("ilan", new Ilan());
        return "ilan-ekle";
    }

    @RequestMapping("/ilan/duzenle/{kod}")
    public String ilanDuzenle(@PathVariable int kod, Model model) {
        model.addAttribute("ilan", ilanService.ilanBul(kod));
        return "ilan-ekle";
    }

    @RequestMapping("/ilan/kaydet")
    public String ilanKaydet(@Valid Ilan ilan, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "ilan-ekle";
        }
        ilan.setUzman(principal.getName());
        ilanService.ilanKaydet(ilan);
        return "redirect:/uzman/ilan/" + ilan.getKod();
    }

    @RequestMapping("/")
    public String tumIlanlar(Model model, Principal principal) {
        model.addAttribute("ilanlar", ilanService.tumIlanlar(principal.getName()));
        return "uzman/ilanlar";
    }

    @RequestMapping("/ilan/{kod}")
    public String ilanBul(@PathVariable int kod, Model model) {
        model.addAttribute("ilan", ilanService.ilanBul(kod));
        return "uzman/ilan";
    }

    @RequestMapping("/ilan/sil/{kod}")
    public String ilanSil(@PathVariable int kod) {
        ilanService.ilanSil(kod);
        return "redirect:/uzman/ilanlar";
    }

}
