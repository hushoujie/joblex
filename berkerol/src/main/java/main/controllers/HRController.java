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
import main.components.Email;
import main.dandelion.Similarity;
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
import org.springframework.web.client.RestTemplate;

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
    public String home() {
        return "/hr/home";
    }

    @RequestMapping("/advert/add")
    public String addAdvert(Model model) {
        model.addAttribute("advert", new Advert());
        return "/hr/advert-save";
    }

    @RequestMapping("/advert/edit/{advertId}")
    public String editAdvert(@PathVariable int advertId, Model model) {
        model.addAttribute("advert", advertService.findAdvert(advertId));
        return "/hr/advert-save";
    }

    @RequestMapping("/advert/save")
    public String saveAdvert(Advert advert, Principal principal) {
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

    @RequestMapping("/blacklist/delete/{applicantId}")
    public String deleteBlacklist(@PathVariable String applicantId) {
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
    public String findAllAdverts(Model model) {
        model.addAttribute("adverts", advertService.findAllAdverts());
        return "/hr/adverts";
    }

    @RequestMapping("/adverts/{applicantId}")
    public String findAllAdverts(@PathVariable String applicantId, Model model) {
        LinkedList<Advert> adverts = new LinkedList<>();
        for (Application application : applicantService.findApplicant(applicantId).getApplications()) {
            adverts.add(application.getAdvert());
        }
        model.addAttribute("adverts", adverts);
        return "/hr/adverts";
    }

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

    @RequestMapping("/applications")
    public String findAllApplications(Model model) {
        LinkedList<Application> applications = new LinkedList<>();
        for (Advert advert : advertService.findAllAdverts()) {
            applications.addAll(advert.getApplications());
        }
        model.addAttribute("applications", applications);
        return "/hr/applications";
    }

    @RequestMapping("/applications/{advertId}")
    public String findAllApplications(@PathVariable int advertId, Model model) {
        List<Application> applications = advertService.findAdvert(advertId).getApplications();
        Collections.sort(applications, new Comparator<Application>() {
            @Override
            public int compare(Application a1, Application a2) {
                return -Double.compare(calcDandelion(a1), calcDandelion(a2));
            }
        });
        model.addAttribute("applications", applications);
        return "/hr/applications";
    }

    @RequestMapping("/application/{applicationId}")
    public String findApplication(@PathVariable int applicationId, Model model) {
        model.addAttribute("application_", applicationService.findApplication(applicationId));
        return "/hr/application";
    }

    @RequestMapping("/applicants")
    public String findAllApplicants(Model model) {
        model.addAttribute("applicants", applicantService.findAllApplicants());
        return "/hr/applicants";
    }

    @RequestMapping("/applicants/{advertId}")
    public String findAllApplicants(@PathVariable int advertId, Model model) {
        model.addAttribute("applicants", findAllApplicantsByAdvert(advertId));
        return "/hr/applicants";
    }

    @RequestMapping("/applicant/{applicantId}")
    public String findApplicant(@PathVariable String applicantId, Model model) {
        model.addAttribute("applicant", applicantService.findApplicant(applicantId));
        return "/hr/applicant";
    }

    @RequestMapping("/search")
    public String searchApplicants(@RequestParam(required = false) String text, Model model) throws SolrServerException, IOException {
        model.addAttribute("address", "/hr/search");
        if (text == null) {
            return "/hr/search";
        }
        model.addAttribute("hitMap", search(text));
        return "/hr/search";
    }

    @RequestMapping("/search/{advertId}")
    public String searchApplicants(@PathVariable int advertId, @RequestParam(required = false) String text, Model model) throws SolrServerException, IOException {
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

    private double calcDandelion(Application application) {
        Advert advert = application.getAdvert();
        Applicant applicant = application.getApplicant();
        String url = "https://api.dandelion.eu/datatxt/sim/v1?text1=" + advert.getUrl()
                + "&text2=" + applicant.getUrl()
                + "&token=a397094f43f840a1ba7f20b875baf5ae&lang=en&bow=always";
        RestTemplate restTemplate = new RestTemplate();
        Similarity s = restTemplate.getForObject(url, Similarity.class);
        return s.getSimilarity();
    }

    private LinkedList<Applicant> findAllApplicantsByAdvert(int advertId) {
        LinkedList<Applicant> applicants = new LinkedList<>();
        for (Application application : advertService.findAdvert(advertId).getApplications()) {
            applicants.add(application.getApplicant());
        }
        return applicants;
    }

    private Map<Heading, Map<String, List<String>>> search(String text) throws SolrServerException, IOException {
        SolrClient solrClient = new HttpSolrClient.Builder("http://localhost:8983/solr/applicants").build();
        SolrQuery query = new SolrQuery(text);
        if (text.contains(":")) {
            query.setHighlightRequireFieldMatch(true);
        }
        query.setHighlight(true);
        query.addHighlightField("*");
        query.setHighlightSimplePost("</strong>");
        query.setHighlightSimplePre("<strong>");
        QueryResponse response = solrClient.query(query);
        Map<Heading, Map<String, List<String>>> hitMap = new TreeMap<>();
        for (Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            Applicant applicant = applicantService.findApplicant(entry.getKey());
            hitMap.put(new Heading(applicant.getFirstname() + ' ' + applicant.getLastname(), entry.getKey()), entry.getValue());
        }
        return hitMap;
    }

}
