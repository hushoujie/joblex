package main;

import main.services.IlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private IlanService ilanService;

    @Autowired
    public void setIlanService(IlanService ilanService) {
        this.ilanService = ilanService;
    }

    @RequestMapping("/giris")
    public String giris() {
        return "uzman/giris";
    }

    @RequestMapping("/")
    public String tumIlanlar(Model model) {
        model.addAttribute("ilanlar", ilanService.tumIlanlar());
        return "aday/ilanlar";
    }

}
