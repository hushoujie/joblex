package main.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.components.Email;
import main.entities.Advert;
import main.entities.Applicant;
import main.entities.Application;
import main.services.AdvertService;
import main.services.ApplicantService;
import main.services.ApplicationService;
import main.solr.Heading;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles all mappings inside HR.
 *
 * @author Berk Erol
 */
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

    /**
     * Handles mappings for adding new adverts.
     *
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/advert/add")
    public String addAdvert(Model model) {
        model.addAttribute("advert", new Advert());
        return "/hr/advert-save";
    }

    /**
     * Handles mapping for editing existing adverts.
     *
     * @param advertId Id of the advert to be edited
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/advert/edit/{advertId}")
    public String editAdvert(@PathVariable int advertId, Model model) {
        model.addAttribute("advert", advertService.findAdvert(advertId));
        return "/hr/advert-save";
    }

    /**
     * Handles mappings for advert saving attempts.
     *
     * @param advert ID of the advert to be saved
     * @param principal principal object for saving the editor of advert
     * @return redirection to advert viewing page
     */
    @RequestMapping("/advert/save")
    public String saveAdvert(Advert advert, Principal principal) {
        advert.setHr(principal.getName());
        advertService.saveAdvert(advert);
        return "redirect:/hr/advert/" + advert.getId();
    }

    /**
     * Handles mappings for application status changing attempts. Fetches and saves the application with the new status
     * and sends an email to the applicant.
     *
     * @param applicationId ID of the application to be edited
     * @param status new status of the application
     * @return redirection to application viewing page
     */
    @RequestMapping("/application/change/{applicationId}")
    public String changeApplicationStatus(@PathVariable int applicationId, @RequestParam int status) {
        Application application = applicationService.findApplication(applicationId);
        application.setStatus(status);
        applicationService.saveApplication(application);
        Applicant applicant = application.getApplicant();
        Email.send(applicant.getEmail(), application.getAdvert().getTitle(), applicant.getFirstname() + ' ' + applicant.getLastname(), application.getStatus());
        return "redirect:/hr/application/" + application.getId();
    }

    /**
     * Handles mappings for applicant blacklisting attempts. Fetches and saves the applicant as blacklisted with its
     * reason then rejects all its applications and sends emails for these changes.
     *
     * @param applicantId ID of the applicant to be blacklisted
     * @param blreason text containing the reason
     * @return redirection to applicant viewing page
     */
    @RequestMapping("/blacklist/add/{applicantId}")
    public String addBlacklist(@PathVariable String applicantId, @RequestParam String blreason) {
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

    /**
     * Handles mappings for blacklist removing attempts. Fetches and saves the applicant as not blacklisted.
     *
     * @param applicantId ID of the applicant to be removed from blacklist
     * @return redirection to applicant viewing page
     */
    @RequestMapping("/blacklist/delete/{applicantId}")
    public String deleteBlacklist(@PathVariable String applicantId) {
        Applicant applicant = applicantService.findApplicant(applicantId);
        applicant.setBlacklist(false);
        applicant.setBlreason(null);
        applicantService.saveApplicant(applicant);
        return "redirect:/hr/applicant/" + applicantId;
    }

    /**
     * Handles mappings for advert deleting attempts.
     *
     * @param advertId ID of the advert to be deleted
     * @return redirection to advert listing page
     */
    @RequestMapping("/advert/delete/{advertId}")
    public String deleteAdvert(@PathVariable int advertId) {
        advertService.deleteAdvert(advertId);
        return "redirect:/hr/adverts";
    }

    /**
     * Handles mappings for listing all adverts.
     *
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/adverts")
    public String findAllAdverts(Model model) {
        model.addAttribute("adverts", advertService.findAllAdverts());
        return "/hr/adverts";
    }

    /**
     * Handles mappings for listing all adverts of an applicant.
     *
     * @param applicantId ID of the applicant to be used
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/adverts/{applicantId}")
    public String findAllAdverts(@PathVariable String applicantId, Model model) {
        LinkedList<Advert> adverts = new LinkedList<>();
        for (Application application : applicantService.findApplicant(applicantId).getApplications()) {
            adverts.add(application.getAdvert());
        }
        model.addAttribute("adverts", adverts);
        return "/hr/adverts";
    }

    /**
     * Handles mappings for listing other applied adverts of an applicant.
     *
     * @param applicationId ID of the applicant to be used
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/others/{applicationId}")
    public String findOtherAdverts(@PathVariable int applicationId, Model model) {
        Application application = applicationService.findApplication(applicationId);
        LinkedList<Advert> adverts = new LinkedList<>();
        for (Application app : application.getApplicant().getApplications()) {
            adverts.add(app.getAdvert());
        }
        adverts.remove(application.getAdvert());
        model.addAttribute("adverts", adverts);
        return "/hr/adverts";
    }

    /**
     * Handles mappings for viewing advert. Also generates all applicants of the advert.
     *
     * @param advertId ID of the advert to be viewed
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/advert/{advertId}")
    public String findAdvert(@PathVariable int advertId, Model model) {
        Advert advert = advertService.findAdvert(advertId);
        LinkedList<Applicant> applicants = new LinkedList<>();
        for (Application application : advert.getApplications()) {
            applicants.add(application.getApplicant());
        }
        model.addAttribute("applicants", applicants);
        model.addAttribute("advert", advert);
        return "/hr/advert";
    }

    /**
     * Handles mappings for listing all applications.
     *
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/applications")
    public String findAllApplications(Model model) {
        model.addAttribute("applications", applicationService.findAllApplications());
        return "/hr/applications";
    }

    /**
     * Handles mappings for listing all applications of an advert. Finds all applications then sorts them according to
     * their similarity scores.
     *
     * @param advertId ID of the advert to be used
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/applications/{advertId}")
    public String findAllApplications(@PathVariable int advertId, Model model) {
        List<Application> applications = advertService.findAdvert(advertId).getApplications();
        Collections.sort(applications, new Comparator<Application>() {
            @Override
            public int compare(Application a1, Application a2) {
                return -Double.compare(a1.getSimilarity(), a2.getSimilarity());
            }
        });
        model.addAttribute("applications", applications);
        return "/hr/applications";
    }

    /**
     * Handles mappings for application similarity updating attempts. Extract keywords of advert and applicants then
     * calculates each application's similarity score.
     *
     * @param advertId ID of the advert to be used for updating applications.
     * @return redirection to application listing page of an advert
     */
    @RequestMapping("/applications/update/{advertId}")
    public String updateApplicationsSimilarities(@PathVariable int advertId) {
        Advert advert = advertService.findAdvert(advertId);
        advert.extractKeywords();
        advertService.saveAdvert(advert);
        for (Application application : advert.getApplications()) {
            Applicant applicant = application.getApplicant();
            applicant.extractKeywords();
            applicantService.saveApplicant(applicant);
            application.calcSimilarity();
            applicationService.saveApplication(application);
        }
        return "redirect:/hr/applications/" + advertId;
    }

    /**
     * Handles mappings for viewing application.
     *
     * @param applicationId ID of the application to be viewed
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/application/{applicationId}")
    public String findApplication(@PathVariable int applicationId, Model model) {
        model.addAttribute("application_", applicationService.findApplication(applicationId));
        return "/hr/application";
    }

    /**
     * Handles mappings for listing all applicants.
     *
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/applicants")
    public String findAllApplicants(Model model) {
        model.addAttribute("applicants", applicantService.findAllApplicants());
        return "/hr/applicants";
    }

    /**
     * Handles mappings for listing all applicants of an advert.
     *
     * @param advertId ID of the advert to be used
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/applicants/{advertId}")
    public String findAllApplicants(@PathVariable int advertId, Model model) {
        model.addAttribute("applicants", findAllApplicantsByAdvert(advertId));
        return "/hr/applicants";
    }

    /**
     * Handles mappings for viewing applicant.
     *
     * @param applicantId Id of the applicant to be viewed
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/applicant/{applicantId}")
    public String findApplicant(@PathVariable String applicantId, Model model) {
        model.addAttribute("applicant", applicantService.findApplicant(applicantId));
        return "/hr/applicant";
    }

    /**
     * Handles mappings for applicant searching attempts.
     *
     * @param text search content
     * @param model model of this page to be transported
     * @return path of HTML file
     */
    @RequestMapping("/search")
    public String searchApplicants(@RequestParam(required = false) String text, Model model) {
        model.addAttribute("address", "/hr/search");
        if (text == null) {
            return "/hr/search";
        }
        model.addAttribute("hitMap", search(text));
        return "/hr/search";
    }

    /**
     * Handles mappings for applicant searching attempts of an advert. Fetches highlight data from Solr server then
     * removes applicants that did not applied to the advert.
     *
     * @param advertId ID of the advert to be used
     * @param text search content
     * @param model model of this page to be transported
     * @return path of HTMl file
     */
    @RequestMapping("/search/{advertId}")
    public String searchApplicants(@PathVariable int advertId, @RequestParam(required = false) String text, Model model) {
        model.addAttribute("address", "/hr/search/" + advertId);
        if (text == null) {
            return "/hr/search";
        }
        LinkedList<Applicant> applicants = findAllApplicantsByAdvert(advertId);
        Map<Heading, Map<String, List<String>>> hitMap = search(text);
        Iterator<Entry<Heading, Map<String, List<String>>>> iterator = hitMap.entrySet().iterator();
        while (iterator.hasNext()) {
            if (!applicants.contains(applicantService.findApplicant(iterator.next().getKey().getId()))) {
                iterator.remove();
            }
        }
        model.addAttribute("hitMap", hitMap);
        return "/hr/search";
    }

    /**
     * Finds all applicants that are applied to the advert.
     *
     * @param advertId ID of the advert to be used
     * @return all applicants as a list
     */
    private LinkedList<Applicant> findAllApplicantsByAdvert(int advertId) {
        LinkedList<Applicant> applicants = new LinkedList<>();
        for (Application application : advertService.findAdvert(advertId).getApplications()) {
            applicants.add(application.getApplicant());
        }
        return applicants;
    }

    /**
     * Fetches highlighted map from Solr server according to the search content then adds a heading object to the map
     * for improving UX.
     *
     * @param text search content
     * @return highlighted map
     */
    private Map<Heading, Map<String, List<String>>> search(String text) {
        SolrClient solrClient = new HttpSolrClient.Builder("http://localhost:8983/solr/applicants").build();
        SolrQuery query = new SolrQuery(text);
        if (text.contains(":")) {
            query.setHighlightRequireFieldMatch(true);
        }
        query.setHighlight(true);
        query.addHighlightField("*");
        query.setHighlightSimplePost("</strong>");
        query.setHighlightSimplePre("<strong>");
        QueryResponse response = new QueryResponse(solrClient);
        try {
            response = solrClient.query(query);
        } catch (SolrServerException | IOException ex) {
            Logger.getLogger(HRController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Map<Heading, Map<String, List<String>>> hitMap = new TreeMap<>();
        for (Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            Applicant applicant = applicantService.findApplicant(entry.getKey());
            hitMap.put(new Heading(applicant.getFirstname() + ' ' + applicant.getLastname(), entry.getKey()), entry.getValue());
        }
        return hitMap;
    }

}
