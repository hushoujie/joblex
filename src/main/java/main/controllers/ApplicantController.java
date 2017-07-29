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

/**
 * Handles all mappings outside HR.
 *
 * @author Berk Erol
 */
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

    /**
     * Handles mappings for home page.
     *
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("logout", connectionRepository.findPrimaryConnection(LinkedIn.class));
        return "/applicant/home";
    }

    /**
     * Handles mappings for HR login attempts.
     *
     * @return path of HTML file
     */
    @RequestMapping("/login")
    public String login() {
        return "/hr/login";
    }

    /**
     * Handles mappings for LinkedIn logout attempts.
     *
     * @return redirection to home page
     */
    @RequestMapping("/logout")
    public String logout() {
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) != null) {
            connectionRepository.removeConnections("linkedin");
        }
        return "redirect:/";
    }

    /**
     * Handles mappings for listing all adverts.
     *
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/adverts")
    public String findAllAdverts(Model model) {
        model.addAttribute("logout", connectionRepository.findPrimaryConnection(LinkedIn.class));
        model.addAttribute("adverts", advertService.findAllAdverts(true));
        return "/applicant/adverts";
    }

    /**
     * Handles mappings for listing most applied adverts. Finds all adverts then sorts them according to their number of
     * applications.
     *
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/adverts/top")
    public String findTopAdverts(Model model) {
        model.addAttribute("logout", connectionRepository.findPrimaryConnection(LinkedIn.class));
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

    /**
     * Handles mappings for listing newly added adverts. Finds all adverts then sorts them according to their IDs.
     *
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/adverts/new")
    public String findNewAdverts(Model model) {
        model.addAttribute("logout", connectionRepository.findPrimaryConnection(LinkedIn.class));
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

    /**
     * Handles mappings for viewing advert. Generates different buttons for already applied, connected, disconnected,
     * blacklisted situations.
     *
     * @param advertId ID of the advert to be viewed
     * @param session current session for redirecting after LinkedIn login
     * @param model model of this page to be transported
     * @return path of HTML file
     * @see setApplicationOptions
     */
    @RequestMapping("/advert/{advertId}")
    public String findAdvert(@PathVariable int advertId, HttpSession session, Model model) {
        model.addAttribute("logout", connectionRepository.findPrimaryConnection(LinkedIn.class));
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) != null) {
            saveLinkedIn(linkedIn.profileOperations().getUserProfileFull());
            Applicant applicant = applicantService.findApplicant(linkedIn.profileOperations().getProfileId());
            if (applicant.getApplications() != null) {
                for (Application application : applicant.getApplications()) {
                    if (application.getAdvert().getId() == advertId) {
                        model = setApplicationOptions(advertId, true, false, false, applicant.isBlacklist(), model);
                        return "/applicant/advert";
                    }
                }
            }
            model = setApplicationOptions(advertId, false, true, false, applicant.isBlacklist(), model);
            return "/applicant/advert";
        }
        session.setAttribute("advert", advertId);
        model = setApplicationOptions(advertId, false, false, true, false, model);
        return "/applicant/advert";
    }

    /**
     * Handles mappings for advert applying attempts. Fetches advert ID and cover letter then generates and saves an
     * application for the advert.
     *
     * @param advertId ID of the advert to be applied
     * @param coverletter cover letter of the applicant
     * @return redirection to advert viewing page with sent information
     */
    @RequestMapping("/apply/{advertId}")
    public String apply(@PathVariable int advertId, @RequestParam String coverletter) {
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) != null) {
            Application application = new Application();
            application.setAdvert(advertService.findAdvert(advertId));
            application.setApplicant(applicantService.findApplicant(linkedIn.profileOperations().getProfileId()));
            application.setStatus(0);
            application.setCoverletter(coverletter);
            application.setSimilarity(0.0);
            applicationService.saveApplication(application);
        }
        return "redirect:/advert/" + advertId + "?sent";
    }

    /**
     * Handles mappings for application canceling attempts.
     *
     * @param applicationId ID of the application to be canceled
     * @return redirection to applicant home page
     */
    @RequestMapping("/application/cancel/{applicationId}")
    public String cancelApplication(@PathVariable int applicationId) {
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) != null) {
            applicationService.deleteApplication(applicationId);
        }
        return "redirect:/applicant/";
    }

    /**
     * Handles mappings for applicant home page for listing applications. Generates different buttons for connected and
     * disconnected situations.
     *
     * @param session current session for redirecting after LinkedIn login
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/applicant/")
    public String findAllApplications(HttpSession session, Model model) {
        model.addAttribute("logout", connectionRepository.findPrimaryConnection(LinkedIn.class));
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) != null) {
            saveLinkedIn(linkedIn.profileOperations().getUserProfileFull());
            model.addAttribute("linkedin", true);
            model.addAttribute("applications", applicantService.findApplicant(linkedIn.profileOperations().getProfileId()).getApplications());
        }
        else {
            model.addAttribute("linkedin", false);
        }
        session.setAttribute("advert", 0);
        return "/applicant/applications";
    }

    /**
     * Handles mappings for viewing application.
     *
     * @param applicationId ID of the application to be viewed
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/applicant/application/{applicationId}")
    public String findApplication(@PathVariable int applicationId, Model model) {
        model.addAttribute("logout", connectionRepository.findPrimaryConnection(LinkedIn.class));
        if (connectionRepository.findPrimaryConnection(LinkedIn.class) != null) {
            model.addAttribute("application_", applicationService.findApplication(applicationId));
        }
        return "/applicant/application";
    }

    /**
     * Configures model for different user situations.
     *
     * @param id ID of the advert to be viewed
     * @param already true if applicant has already applied to the advert
     * @param apply true if applicant is connected but has not applied yet
     * @param connect true if applicant is not connected
     * @param blacklist true if applicant is blacklisted
     * @param model model of this page to be transported
     * @return model of this page to be transported
     */
    private Model setApplicationOptions(int id, boolean already, boolean apply, boolean connect, boolean blacklist, Model model) {
        model.addAttribute("advert", advertService.findAdvert(id));
        model.addAttribute("already", already);
        model.addAttribute("apply", apply);
        model.addAttribute("connect", connect);
        model.addAttribute("blacklist", blacklist);
        return model;
    }

    /**
     * Fetches LinkedIn profile details and saves to MySQL server for later use in Solr server
     *
     * @param profileFull LinkedIn profile object of the connected applicant
     */
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
