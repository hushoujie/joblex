package main.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import main.entities.Advert;
import main.entities.Applicant;
import main.entities.Application;
import main.entities.Experience;
import main.services.AdvertService;
import main.services.ApplicantService;
import main.services.ApplicationService;
import main.services.ExperienceService;
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
public class ApplicantController {

    private final LinkedIn linkedIn;

    private final ConnectionRepository connectionRepository;

    private AdvertService advertService;

    private ApplicantService applicantService;

    private ApplicationService applicationService;

    private ExperienceService experienceService;

    @Inject
    public ApplicantController(LinkedIn linkedIn, ConnectionRepository connectionRepository) {
        this.linkedIn = linkedIn;
        this.connectionRepository = connectionRepository;
    }

    @Autowired
    public void setAdvertService(AdvertService advertService) {
        this.advertService = advertService;
    }

    @Autowired
    public void setApplicantService(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @Autowired
    public void setApplicationService(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Autowired
    public void setExperienceService(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @RequestMapping("/")
    public String home(Model model) {
        return "/applicant/home";
    }

    @RequestMapping("/login")
    public String login() {
        return "/hr/login";
    }

    @RequestMapping("/adverts")
    public String findAllAdverts(Model model) {
        model.addAttribute("adverts", advertService.findAllAdverts(true));
        return "/applicant/adverts";
    }

    @RequestMapping("/adverts/top")
    public String findTopAdverts(Model model) {
        LinkedList<Advert> adverts = advertService.findAllAdverts(true);
        Collections.sort(adverts, new Comparator<Advert>() {
            @Override
            public int compare(Advert a1, Advert a2) {
                return -Integer.compare(a1.getApplications().size(), a2.getApplications().size());
            }
        });
        model.addAttribute("adverts", adverts);
        return "/applicant/adverts";
    }

    @RequestMapping("/adverts/new")
    public String findNewAdverts(Model model) {
        LinkedList<Advert> adverts = advertService.findAllAdverts(true);
        Collections.sort(adverts, new Comparator<Advert>() {
            @Override
            public int compare(Advert a1, Advert a2) {
                return -Integer.compare(a1.getId(), a2.getId());
            }
        });
        model.addAttribute("adverts", adverts);
        return "/applicant/adverts";
    }

    @RequestMapping("/advert/{advertId}")
    public String findAdvert(@PathVariable int advertId, HttpSession session, Model model) {
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) != null) {
            saveLinkedIn(linkedIn.profileOperations().getUserProfileFull());
            Applicant applicant = applicantService.findApplicant(linkedIn.profileOperations().getProfileId());
            for (Application application : applicant.getApplications()) {
                if (application.getAdvert().getId() == advertId) {
                    model = setApplicationOptions(advertId, true, false, false, applicant.isBlacklist(), model);
                    return "/applicant/advert";
                }
            }
            model = setApplicationOptions(advertId, false, true, false, applicant.isBlacklist(), model);
            return "/applicant/advert";
        }
        session.setAttribute("advert", advertId);
        model = setApplicationOptions(advertId, false, false, true, false, model);
        return "/applicant/advert";
    }

    @RequestMapping("/apply/{advertId}")
    public String apply(@PathVariable int advertId, @RequestParam String coverletter, Model model) {
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) != null) {
            Application application = new Application();
            application.setAdvert(advertService.findAdvert(advertId));
            application.setApplicant(applicantService.findApplicant(linkedIn.profileOperations().getProfileId()));
            application.setStatus(0);
            application.setCoverletter(coverletter);
            applicationService.saveApplication(application);
        }
        return "redirect:/advert/" + advertId + "?sent";
    }

    @RequestMapping("/application/cancel/{applicationId}")
    public String cancelApplication(@PathVariable int applicationId, Model model) {
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) != null) {
            applicationService.deleteApplication(applicationId);
        }
        return "redirect:/applicant/";
    }

    @RequestMapping("/applicant/")
    public String findAllApplications(HttpSession session, Model model) {
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) == null) {
            model.addAttribute("linkedin", false);
        }
        else {
            saveLinkedIn(linkedIn.profileOperations().getUserProfileFull());
            model.addAttribute("linkedin", true);
            model.addAttribute("applications", applicantService.findApplicant(linkedIn.profileOperations().getProfileId()).getApplications());
        }
        session.setAttribute("advert", 0);
        return "/applicant/applications";
    }

    @RequestMapping("/applicant/application/{applicationId}")
    public String findApplication(@PathVariable int applicationId, Model model) {
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) != null) {
            model.addAttribute("application_", applicationService.findApplication(applicationId));
        }
        return "/applicant/application";
    }

    private Model setApplicationOptions(int id, boolean already, boolean apply, boolean connect, boolean blacklist, Model model) {
        model.addAttribute("advert", advertService.findAdvert(id));
        model.addAttribute("already", already);
        model.addAttribute("apply", apply);
        model.addAttribute("connect", connect);
        model.addAttribute("blacklist", blacklist);
        return model;
    }

    private void saveLinkedIn(LinkedInProfileFull profileFull) {
        Applicant applicant = applicantService.findApplicant(profileFull.getId());
        if (applicant == null) {
            applicant = new Applicant();
            applicant.setId(profileFull.getId());
        }
        applicant.setPhoto(profileFull.getProfilePictureUrl());
        applicant.setFirstname(profileFull.getFirstName());
        applicant.setLastname(profileFull.getLastName());
        applicant.setIndustry(profileFull.getIndustry());
        applicant.setLocation(profileFull.getLocation().getName());
        applicant.setEmail(profileFull.getEmailAddress());
        applicant.setCountry(profileFull.getLocation().getCountry());
        applicant.setHeadline(profileFull.getHeadline());
        applicant.setSummary(profileFull.getSummary());
        applicantService.saveApplicant(applicant);
        for (Position position : profileFull.getPositions()) {
            LinkedInDate startdate = position.getStartDate();
            Experience experience = experienceService.findExperience(applicant, position.getId());
            if (experience == null) {
                experience = new Experience();
            }
            experience.setLinkedinid(position.getId());
            experience.setApplicant(applicantService.findApplicant(profileFull.getId()));
            experience.setCompany(position.getCompany().getName());
            experience.setPosition(position.getTitle());
            experience.setSummary(position.getSummary());
            experience.setStartdate(new Date(startdate.getYear() - 1900, startdate.getMonth() - 1, 1));
            experienceService.saveExperience(experience);
        }
    }

}
