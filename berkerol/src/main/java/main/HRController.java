package main;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import javax.validation.Valid;
import main.entities.Advert;
import main.entities.Applicant;
import main.entities.Application;
import main.services.AdvertService;
import main.services.ApplicantService;
import main.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/hr")
public class HRController {

    private AdvertService advertService;

    private ApplicantService applicantService;

    private ApplicationService applicationService;

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

    @RequestMapping("/")
    public String home(Model model, Principal principal) {
        model.addAttribute("hrname", principal.getName());
        return "/hr/home";
    }

    @RequestMapping("/advert/add")
    public String addAdvert(Model model, Principal principal) {
        model.addAttribute("hrname", principal.getName());
        model.addAttribute("advert", new Advert());
        return "/hr/advert-save";
    }

    @RequestMapping("/advert/edit/{advertId}")
    public String editAdvert(@PathVariable int advertId, Model model, Principal principal) {
        model.addAttribute("hrname", principal.getName());
        model.addAttribute("advert", advertService.findAdvert(advertId));
        return "/hr/advert-save";
    }

    @RequestMapping("/advert/save")
    public String saveAdvert(@Valid Advert advert, BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("hrname", principal.getName());
            return "/hr/advert-save";
        }
        advert.setHr(principal.getName());
        advertService.saveAdvert(advert);
        return "redirect:/hr/advert/" + advert.getId();
    }

    @RequestMapping("/application/change/{applicationId}")
    public String changeApplicationStatus(@PathVariable int applicationId, @RequestParam int status) {
        Application application = applicationService.findApplication(applicationId);
        application.setStatus(status);
        applicationService.saveApplication(application);
        Applicant applicant = application.getApplicant();
        Email.send(applicant.getEmail(), application.getAdvert().getTitle(), applicant.getFirstname() + ' ' + applicant.getLastname(), application.getStatus());
        return "redirect:/hr/application/" + application.getId();
    }

    @RequestMapping("/blacklist/add/{applicantId}")
    public String addBlacklist(@PathVariable String applicantId, @RequestParam String blreason, Model model) {
        Applicant applicant = applicantService.findApplicant(applicantId);
        applicant.setBlacklist(true);
        applicant.setBlreason(blreason);
        applicantService.saveApplicant(applicant);
        for (Application application : applicant.getApplications()) {
            application.setStatus(3);
            applicationService.saveApplication(application);
            Email.send(applicant.getEmail(), application.getAdvert().getTitle(), applicant.getFirstname() + ' ' + applicant.getLastname(), application.getStatus());
        }
        return "redirect:/hr/applicant/" + applicantId;
    }

    @RequestMapping("/blacklist/delete/{applicantId}")
    public String deleteBlacklist(@PathVariable String applicantId, Model model) {
        Applicant applicant = applicantService.findApplicant(applicantId);
        applicant.setBlacklist(false);
        applicant.setBlreason(null);
        applicantService.saveApplicant(applicant);
        return "redirect:/hr/applicant/" + applicantId;
    }

    @RequestMapping("/advert/delete/{advertId}")
    public String deleteAdvert(@PathVariable int advertId) {
        advertService.deleteAdvert(advertId);
        return "redirect:/hr/adverts";
    }

    @RequestMapping("/adverts")
    public String findAllAdverts(Model model, Principal principal) {
        model.addAttribute("hrname", principal.getName());
        model.addAttribute("adverts", advertService.findAllAdverts());
        return "/hr/adverts";
    }

    @RequestMapping("/adverts/{applicantId}")
    public String findAllAdverts(@PathVariable String applicantId, Model model, Principal principal) {
        model.addAttribute("hrname", principal.getName());
        LinkedList<Advert> adverts = new LinkedList<>();
        for (Application application : applicantService.findApplicant(applicantId).getApplications()) {
            adverts.add(application.getAdvert());
        }
        model.addAttribute("adverts", adverts);
        return "/hr/adverts";
    }

    @RequestMapping("/others/{applicationId}")
    public String findOtherAdverts(@PathVariable int applicationId, Model model, Principal principal) {
        model.addAttribute("hrname", principal.getName());
        Application application = applicationService.findApplication(applicationId);
        LinkedList<Advert> adverts = new LinkedList<>();
        for (Application app : application.getApplicant().getApplications()) {
            adverts.add(app.getAdvert());
        }
        adverts.remove(application.getAdvert());
        model.addAttribute("adverts", adverts);
        return "/hr/adverts";
    }

    @RequestMapping("/advert/{advertId}")
    public String findAdvert(@PathVariable int advertId, Model model, Principal principal) {
        model.addAttribute("hrname", principal.getName());
        Advert advert = advertService.findAdvert(advertId);
        LinkedList<Applicant> applicants = new LinkedList<>();
        for (Application application : advert.getApplications()) {
            applicants.add(application.getApplicant());
        }
        model.addAttribute("applicants", applicants);
        model.addAttribute("advert", advert);
        return "/hr/advert";
    }

    @RequestMapping("/applications")
    public String findAllApplications(@RequestParam(required = false, defaultValue = "false") boolean waiting,
            @RequestParam(required = false, defaultValue = "false") boolean processing, @RequestParam(required = false, defaultValue = "false") boolean accepted,
            @RequestParam(required = false, defaultValue = "false") boolean rejected, Model model, Principal principal) {
        model.addAttribute("hrname", principal.getName());
        LinkedList<Application> applications = new LinkedList<>();
        for (Advert advert : advertService.findAllAdverts()) {
            applications.addAll(advert.getApplications());
        }
        model = filterApplications(waiting, processing, accepted, rejected, applications, model);
        model.addAttribute("address", "/hr/applications");
        return "/hr/applications";
    }

    @RequestMapping("/applications/{advertId}")
    public String findAllApplications(@PathVariable int advertId, @RequestParam(required = false, defaultValue = "false") boolean waiting,
            @RequestParam(required = false, defaultValue = "false") boolean processing, @RequestParam(required = false, defaultValue = "false") boolean accepted,
            @RequestParam(required = false, defaultValue = "false") boolean rejected, Model model, Principal principal) {
        model.addAttribute("hrname", principal.getName());
        model = filterApplications(waiting, processing, accepted, rejected, advertService.findAdvert(advertId).getApplications(), model);
        model.addAttribute("address", "/hr/applications/" + advertId);
        return "/hr/applications";
    }

    private Model filterApplications(boolean waiting, boolean processing, boolean accepted, boolean rejected, List<Application> allApplications, Model model) {
        if (!waiting && !processing && !accepted && !rejected) {
            waiting = true;
            processing = true;
            accepted = true;
            rejected = true;
        }
        LinkedList<Application> applications = new LinkedList<>();
        for (Application application : allApplications) {
            if (waiting && application.getStatus() == 0) {
                applications.add(application);
            }
            if (processing && application.getStatus() == 1) {
                applications.add(application);
            }
            if (accepted && application.getStatus() == 2) {
                applications.add(application);
            }
            if (rejected && application.getStatus() == 3) {
                applications.add(application);
            }
        }
        model.addAttribute("waiting", waiting);
        model.addAttribute("processing", processing);
        model.addAttribute("accepted", accepted);
        model.addAttribute("rejected", rejected);
        model.addAttribute("applications", applications);
        return model;
    }

    @RequestMapping("/application/{applicationId}")
    public String findApplication(@PathVariable int applicationId, Model model, Principal principal) {
        model.addAttribute("hrname", principal.getName());
        model.addAttribute("application_", applicationService.findApplication(applicationId));
        return "/hr/application";
    }

    @RequestMapping("/applicants")
    public String findAllApplicants(Model model, Principal principal) {
        model.addAttribute("hrname", principal.getName());
        model.addAttribute("applicants", applicantService.findAllApplicants());
        return "/hr/applicants";
    }

    @RequestMapping("/applicants/{advertId}")
    public String findAllApplicants(@PathVariable int advertId, Model model, Principal principal) {
        model.addAttribute("hrname", principal.getName());
        LinkedList<Applicant> applicants = new LinkedList<>();
        for (Application application : advertService.findAdvert(advertId).getApplications()) {
            applicants.add(application.getApplicant());
        }
        model.addAttribute("applicants", applicants);
        return "/hr/applicants";
    }

    @RequestMapping("/applicant/{applicantId}")
    public String findApplicant(@PathVariable String applicantId, Model model, Principal principal) {
        model.addAttribute("hrname", principal.getName());
        model.addAttribute("applicant", applicantService.findApplicant(applicantId));
        return "/hr/applicant";
    }

}
