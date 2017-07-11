package deneme;

import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/aday")
public class AdayController {

    private AdayService adayService;

    private final LinkedIn linkedIn;

    private final ConnectionRepository connectionRepository;

    @Inject
    public AdayController(LinkedIn linkedIn, ConnectionRepository connectionRepository) {
        this.linkedIn = linkedIn;
        this.connectionRepository = connectionRepository;
    }

    @Autowired
    public void setAdayService(AdayService adayService) {
        this.adayService = adayService;
    }

    @RequestMapping("/")
    public String anaSayfa(Model model) {
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) == null) {
            return "redirect:/connect/linkedin";
        }
        LinkedInProfile profile = linkedIn.profileOperations().getUserProfile();
        model.addAttribute("email", profile.getEmailAddress());
        model.addAttribute("firstname", profile.getFirstName());
        model.addAttribute("headline", profile.getHeadline());
        model.addAttribute("id", profile.getId());
        model.addAttribute("industry", profile.getIndustry());
        model.addAttribute("lastname", profile.getLastName());
        model.addAttribute("summary", profile.getSummary());
        LinkedInProfileFull profileFull = linkedIn.profileOperations().getUserProfileFull();
        model.addAttribute("location", profileFull.getLocation().getName());
        model.addAttribute("country", profileFull.getLocation().getCountry());
        model.addAttribute("positions", profileFull.getPositions());
        Aday aday;
        if(adayService.adayBul(profile.getId()) == null) {
            aday = new Aday();
        }
        else {
            aday = adayService.adayBul(profile.getId());
        }
        aday.setEmail(profile.getEmailAddress());
        aday.setFirstname(profile.getFirstName());
        aday.setHeadline(profile.getHeadline());
        aday.setId(profile.getId());
        aday.setIndustry(profile.getIndustry());
        aday.setLastname(profile.getLastName());
        aday.setSummary(profile.getSummary());
        aday.setLocation(profileFull.getLocation().getName());
        aday.setCountry(profileFull.getLocation().getCountry());
        adayService.adayKaydet(aday);
        return "aday/linkedin";
    }

}
