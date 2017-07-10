package deneme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private IlanService ilanService;

    @Autowired
    public void setIlanService(IlanService ilanService) {
        this.ilanService = ilanService;
    }

    @RequestMapping("/")
    public String anaSayfa() {
        return "aday/ana-sayfa";
    }

    @RequestMapping("/giris")
    public String giris() {
        return "giris";
    }
    
    @RequestMapping("/ilanlar")
    public String tumIlanlar(Model model) {
        model.addAttribute("ilanlar", ilanService.tumIlanlar());
        return "aday/ilanlar";
    }

    @RequestMapping("/ilan/{kod}")
    public String ilanBul(@PathVariable int kod, Model model) {
        model.addAttribute("ilan", ilanService.ilanBul(kod));
        return "aday/ilan";
    }

}
