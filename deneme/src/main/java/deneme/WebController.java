package deneme;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class WebController extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/ilan-eklendi").setViewName("ilan-eklendi");
    }

    @GetMapping("/ilan-ekle")
    public String showForm(Ilan ilan) {
        return "ilan-ekle";
    }

    @PostMapping("/ilan-ekle")
    public String checkIlan(@Valid Ilan ilan, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "ilan-ekle";
        }

        return "redirect:/ilan-eklendi";
    }
}
