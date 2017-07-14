package main;

import java.util.Date;
import javax.inject.Inject;
import main.entities.Aday;
import main.entities.Basvuru;
import main.entities.Pozisyon;
import main.services.AdayService;
import main.services.BasvuruService;
import main.services.IlanService;
import main.services.PozisyonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInDate;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.linkedin.api.Position;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdayController {

    private final LinkedIn linkedIn;

    private final ConnectionRepository connectionRepository;

    private AdayService adayService;

    private BasvuruService basvuruService;

    private IlanService ilanService;

    private PozisyonService pozisyonService;

    @Inject
    public AdayController(LinkedIn linkedIn, ConnectionRepository connectionRepository) {
        this.linkedIn = linkedIn;
        this.connectionRepository = connectionRepository;
    }

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

    @RequestMapping("/ilan/{ilanKodu}")
    public String ilanBul(@PathVariable int ilanKodu, Model model) {
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) != null) {
            Aday aday = adayService.adayBul(linkedIn.profileOperations().getProfileId());
            boolean karaliste = aday.isKaraliste();
            for (Basvuru basvuru : aday.getBasvurular()) {
                if (basvuru.getIlan().getKod() == ilanKodu) {
                    model = setBasvuruAttributes(ilanKodu, true, false, false, karaliste, model);
                    return "/aday/ilan";
                }
            }
            model = setBasvuruAttributes(ilanKodu, false, false, true, karaliste, model);
            return "/aday/ilan";
        }
        model = setBasvuruAttributes(ilanKodu, false, true, false, false, model);
        return "/aday/ilan";
    }

    @RequestMapping("/basvur/{ilanKodu}")
    public String basvur(@PathVariable int ilanKodu, Model model) {
        Basvuru basvuru = new Basvuru();
        basvuru.setAday(adayService.adayBul(linkedIn.profileOperations().getProfileId()));
        basvuru.setIlan(ilanService.ilanBul(ilanKodu));
        basvuru.setDurum(0);
        basvuruService.basvuruKaydet(basvuru);
        linkedinKaydet(linkedIn.profileOperations().getUserProfileFull());
        return "redirect:/ilan/" + ilanKodu + "?iletildi";
    }

    @RequestMapping("/aday/")
    public String tumBasvurular(Model model) {
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) == null) {
            model.addAttribute("linkedin", false);
        }
        else {
            model.addAttribute("linkedin", true);
            model.addAttribute("basvurular", adayService.adayBul(linkedIn.profileOperations().getProfileId()).getBasvurular());
        }
        return "/aday/basvurular";
    }

    @RequestMapping("/aday/basvuru/{basvuruKodu}")
    public String basvuruBul(@PathVariable Integer basvuruKodu, Model model) {
        model.addAttribute("basvuru", basvuruService.basvuruBul(basvuruKodu));
        return "/aday/basvuru";
    }

    private Model setBasvuruAttributes(int kod, boolean uyari, boolean baglan, boolean basvur, boolean karaliste, Model model) {
        model.addAttribute("ilan", ilanService.ilanBul(kod));
        model.addAttribute("uyari", uyari);
        model.addAttribute("baglan", baglan);
        model.addAttribute("basvur", basvur);
        model.addAttribute("karaliste", karaliste);
        return model;
    }

    private void linkedinKaydet(LinkedInProfileFull profileFull) {
        Aday aday;
        if (adayService.adayBul(profileFull.getId()) == null) {
            aday = new Aday();
        }
        else {
            aday = adayService.adayBul(profileFull.getId());
        }
        aday.setEmail(profileFull.getEmailAddress());
        aday.setFirstname(profileFull.getFirstName());
        aday.setHeadline(profileFull.getHeadline());
        aday.setId(profileFull.getId());
        aday.setIndustry(profileFull.getIndustry());
        aday.setLastname(profileFull.getLastName());
        aday.setSummary(profileFull.getSummary());
        aday.setLocation(profileFull.getLocation().getName());
        aday.setCountry(profileFull.getLocation().getCountry());
        adayService.adayKaydet(aday);
        for (Position position : profileFull.getPositions()) {
            LinkedInDate startdate = position.getStartDate();
            Pozisyon pozisyon;
            if (pozisyonService.pozisyonBul(position.getId()) == null) {
                pozisyon = new Pozisyon();
            }
            else {
                pozisyon = pozisyonService.pozisyonBul(position.getId());
            }
            pozisyon.setAday(adayService.adayBul(profileFull.getId()));
            pozisyon.setCompany(position.getCompany().getName());
            pozisyon.setId(position.getId());
            pozisyon.setIscurrent(position.getIsCurrent());
            pozisyon.setStartdate(new Date(startdate.getYear() - 1900, startdate.getMonth() - 1, 1));
            pozisyon.setSummary(position.getSummary());
            pozisyon.setTitle(position.getTitle());
            pozisyonService.pozisyonKaydet(pozisyon);
        }
    }

}
