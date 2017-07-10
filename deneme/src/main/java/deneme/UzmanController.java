package deneme;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/uzman")
public class UzmanController {

    @RequestMapping("/")
    public String anaSayfa() {
        return "uzman/ana-sayfa";
    }

}
