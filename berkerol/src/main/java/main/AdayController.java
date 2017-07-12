package main;

import java.util.Date;
import java.util.TreeMap;
import javax.inject.Inject;
import main.domains.Aday;
import main.domains.Basvuru;
import main.domains.Ilan;
import main.domains.Pozisyon;
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
import org.springframework.web.bind.annotation.RequestParam;

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

    private Model setBasvuruAttributes(int kod, boolean uyari, boolean bilgi, boolean baglan, boolean basvur, Model model) {
        model.addAttribute("ilan", ilanService.ilanBul(kod));
        model.addAttribute("uyari", uyari);
        model.addAttribute("bilgi", bilgi);
        model.addAttribute("baglan", baglan);
        model.addAttribute("basvur", basvur);
        return model;
    }

    @RequestMapping("/ilan/{kod}")
    public String ilanBul(@PathVariable int kod, @RequestParam(required = false, defaultValue = "false") boolean iletildi, Model model) {
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) != null) {
            if (!iletildi) {
                for (Basvuru basvuru : basvuruService.tumBasvurular(linkedIn.profileOperations().getProfileId())) {
                    if (basvuru.getIlan() == kod) {
                        model = setBasvuruAttributes(kod, true, false, false, false, model);
                        return "aday/ilan";
                    }
                }
                model = setBasvuruAttributes(kod, false, false, false, true, model);
                return "aday/ilan";
            }
            else {
                model = setBasvuruAttributes(kod, false, true, false, false, model);
                return "aday/ilan";
            }
        }
        model = setBasvuruAttributes(kod, false, false, true, false, model);
        return "aday/ilan";
    }

    @RequestMapping("/basvur/{kod}")
    public String basvur(@PathVariable int kod, Model model) {
        Basvuru basvuru = new Basvuru();
        basvuru.setAday(linkedIn.profileOperations().getProfileId());
        basvuru.setIlan(kod);
        basvuru.setDurum(0);
        basvuruService.basvuruKaydet(basvuru);
        return "redirect:/ilan/" + kod + "?iletildi=true";
    }

    @RequestMapping("/aday/")
    public String tumBasvurular(Model model) {
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) == null) {
            model.addAttribute("linkedin", false);
        }
        else {
            model.addAttribute("linkedin", true);
            TreeMap<Basvuru, Ilan> basvurular = new TreeMap<>();
            for (Basvuru basvuru : basvuruService.tumBasvurular(linkedIn.profileOperations().getProfileId())) {
                basvurular.put(basvuru, ilanService.ilanBul(basvuru.getIlan()));
            }
            model.addAttribute("basvurular", basvurular);
            linkedinKaydet(linkedIn.profileOperations().getUserProfileFull());
        }
        return "aday/basvurular";
    }

    @RequestMapping("/aday/basvuru/{kod}")
    public String basvuruBul(@PathVariable Integer kod, Model model) {
        Basvuru basvuru = basvuruService.basvuruBul(kod);
        model.addAttribute("basvuru", basvuru);
        model.addAttribute("ilan", ilanService.ilanBul(basvuru.getIlan()));
        return "aday/basvuru";
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
            pozisyon.setAday(profileFull.getId());
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
