package com.berkerol.joblex;

import com.berkerol.joblex.elasticsearch.Profile;
import com.berkerol.joblex.entities.*;
import com.berkerol.joblex.services.*;
import com.berkerol.joblex.util.MailSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/")
public class JoblexController {

    private final MailSender mailSender;

    private final ObjectMapper objectMapper;

    private final RestHighLevelClient restHighLevelClient;

    private final ApplicationService applicationService;

    private final AuthorityService authorityService;

    private final BlacklistService blacklistService;

    private final EducationService educationService;

    private final ExperienceService experienceService;

    private final JobService jobService;

    private final SavedJobService savedJobService;

    private final TokenService tokenService;

    private final UserService userService;

    @Value("${app.url}")
    private String appUrl;

    public JoblexController(MailSender mailSender, ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient, ApplicationService applicationService,
                            AuthorityService authorityService, BlacklistService blacklistService, EducationService educationService, ExperienceService experienceService,
                            JobService jobService, SavedJobService savedJobService, TokenService tokenService, UserService userService) {
        this.mailSender = mailSender;
        this.objectMapper = objectMapper;
        this.restHighLevelClient = restHighLevelClient;
        this.applicationService = applicationService;
        this.authorityService = authorityService;
        this.blacklistService = blacklistService;
        this.educationService = educationService;
        this.experienceService = experienceService;
        this.jobService = jobService;
        this.savedJobService = savedJobService;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @RequestMapping("signin")
    public String signin() {
        return "signin";
    }

    @GetMapping("signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("signup")
    public String signup(@RequestParam String email, @RequestParam String password, @RequestParam String type) {
        if (userService.findByUsername(email) != null) {
            return "redirect:/signup?alreadysignedup";
        }
        User user = new User(email, new BCryptPasswordEncoder().encode(password), false);
        userService.save(user);
        authorityService.save(new Authority(email, type.equals("candidate") ? "USER" : "ADMIN"));
        String token = UUID.randomUUID().toString();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        tokenService.save(new Token(user, token, calendar.getTime()));
        mailSender.send(email, email, "Joblex Account Verification", appUrl + "/confirm/" + token);
        return "redirect:/signup?versent";
    }

    @RequestMapping("confirm/{token}")
    public String confirm(@PathVariable String token) {
        Token tokenObject = tokenService.findByToken(token);
        if (tokenObject == null) {
            return "redirect:/signup?invalidtoken";
        }
        if (tokenObject.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime() <= 0) {
            return "redirect:/signup?tokenexpired";
        }
        User user = tokenObject.getUser();
        if (user.isEnabled()) {
            return "redirect:/signup?alreadyverified";
        }
        user.setEnabled(true);
        userService.save(user);
        return "redirect:/signin?verified";
    }

    private boolean isRecruiter(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated() && authentication.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals("ADMIN"));
    }

    private ArrayList<User> markBlacklistedCandidates(ArrayList<User> profiles, Principal principal) {
        ArrayList<Long> blacklistedCandidates = new ArrayList<>();
        for (Blacklist blacklist : blacklistService.findAllByRecruiterId(userService.findByUsername(principal.getName()).getId())) {
            blacklistedCandidates.add(blacklist.getCandidateId());
        }
        for (User candidate : profiles) {
            if (blacklistedCandidates.contains(candidate.getId())) {
                candidate.setBlacklist(true);
            }
        }
        return profiles;
    }

    private <T> List<T> search(Class<T> clazz, String query, String index) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.queryStringQuery(query));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<T> list = new ArrayList<>();
        Arrays.stream(searchResponse.getHits().getHits()).forEach(hit -> list.add(objectMapper.convertValue(hit.getSourceAsMap(), clazz)));
        return list;
    }

    @RequestMapping
    public String searchJobs(@RequestParam(value = "q", required = false) String query, Model model) throws IOException {
        model.addAttribute("recruiter", false);
        model.addAttribute("search", true);
        if (query == null) {
            model.addAttribute("jobs", new ArrayList<>());
            return "jobs";
        }
        model.addAttribute("jobs", search(com.berkerol.joblex.elasticsearch.Job.class, query, "job"));
        return "jobs";
    }

    @RequestMapping("jobs")
    public String getAllJobs(Model model) {
        model.addAttribute("jobs", jobService.findAllByStatus(true));
        model.addAttribute("recruiter", false);
        model.addAttribute("search", false);
        return "jobs";
    }

    @RequestMapping("recruiter/search")
    public String searchProfiles(@RequestParam(value = "q", required = false) String query, Model model, Principal principal) throws IOException {
        model.addAttribute("search", true);
        if (query == null) {
            model.addAttribute("profiles", new ArrayList<>());
            return "profiles";
        }
        ArrayList<Long> blacklistedCandidates = new ArrayList<>();
        for (Blacklist blacklist : blacklistService.findAllByRecruiterId(userService.findByUsername(principal.getName()).getId())) {
            blacklistedCandidates.add(blacklist.getCandidateId());
        }
        List<Profile> profiles = search(Profile.class, query, "profile");
        for (Profile profile : profiles) {
            if (blacklistedCandidates.contains(profile.getId())) {
                profile.setBlacklist(true);
            }
        }
        model.addAttribute("profiles", profiles);
        return "profiles";
    }

    @RequestMapping("profiles")
    public String getAllProfiles(Model model, Principal principal, Authentication authentication) {
        ArrayList<User> profiles = new ArrayList<>();
        for (Authority authority : authorityService.findAllByAuthority("USER")) {
            User user = userService.findByUsername(authority.getUsername());
            user.setBlacklist(false);
            profiles.add(user);
        }
        if (isRecruiter(authentication)) {
            model.addAttribute("profiles", markBlacklistedCandidates(profiles, principal));
        } else {
            model.addAttribute("profiles", profiles);
        }
        model.addAttribute("search", false);
        return "profiles";
    }

    @RequestMapping("candidate/jobs")
    public String getSavedJobs(Model model, Principal principal) {
        ArrayList<Job> savedJobs = new ArrayList<>();
        for (SavedJob savedJob : userService.findByUsername(principal.getName()).getSavedJobs()) {
            savedJobs.add(savedJob.getJob());
        }
        model.addAttribute("jobs", savedJobs);
        model.addAttribute("recruiter", false);
        model.addAttribute("search", false);
        return "jobs";
    }

    @RequestMapping("recruiter/jobs")
    public String getCreatedJobs(Model model, Principal principal) {
        model.addAttribute("jobs", userService.findByUsername(principal.getName()).getCreatedJobs());
        model.addAttribute("recruiter", true);
        model.addAttribute("search", false);
        return "jobs";
    }

    @RequestMapping("candidate/applications")
    public String getCandidateApplications(Model model, Principal principal) {
        model.addAttribute("applications", userService.findByUsername(principal.getName()).getApplications());
        return "applications";
    }

    @RequestMapping("recruiter/applications")
    public String getRecruiterApplications(Model model, Principal principal) {
        ArrayList<Application> applications = new ArrayList<>();
        for (Job job : userService.findByUsername(principal.getName()).getCreatedJobs()) {
            applications.addAll(job.getApplications());
        }
        model.addAttribute("applications", applications);
        return "applications";
    }

    @RequestMapping("recruiter/job/create")
    public String createJob(Model model) {
        model.addAttribute("job", new Job());
        return "job-edit";
    }

    @RequestMapping("recruiter/job/edit/{jobId}")
    public String editJob(@PathVariable long jobId, Model model) {
        model.addAttribute("job", jobService.findById(jobId));
        return "job-edit";
    }

    @RequestMapping("recruiter/job/save")
    public String saveJob(Job job, Principal principal) {
        if (job.getUser().getId() == userService.findByUsername(principal.getName()).getId()) {
            jobService.save(job);
        }
        return "redirect:/job/" + job.getId();
    }

    @RequestMapping("recruiter/job/delete/{jobId}")
    public String deleteJob(@PathVariable long jobId, Principal principal) {
        if (jobService.findById(jobId).getUser().getId() == userService.findByUsername(principal.getName()).getId()) {
            jobService.deleteById(jobId);
        }
        return "redirect:/jobs";
    }

    @RequestMapping("job/{jobId}")
    public String getJob(@PathVariable long jobId, Model model, Principal principal, Authentication authentication) {
        Job job = jobService.findById(jobId);
        User user = userService.findByUsername(principal.getName());
        boolean recruiter = isRecruiter(authentication);
        boolean blacklisted = false;
        boolean applied = false;
        boolean saved = false;
        if (!recruiter && authentication != null && authentication.isAuthenticated()) {
            for (Blacklist blacklist : blacklistService.findAllByRecruiterId(job.getUser().getId())) {
                if (blacklist.getCandidateId() == user.getId()) {
                    blacklisted = true;
                    break;
                }
            }
            if (!blacklisted) {
                for (Application application : user.getApplications()) {
                    if (application.getJob().equals(job)) {
                        applied = true;
                        break;
                    }
                }
                if (!applied) {
                    for (SavedJob savedJob : user.getSavedJobs()) {
                        if (savedJob.getJob().equals(job)) {
                            saved = true;
                            model.addAttribute("notes", savedJob.getNotes());
                            break;
                        }
                    }
                }
            }
        }
        model.addAttribute("job", job);
        model.addAttribute("recruiter", recruiter && job.getUser().getId() == user.getId());
        model.addAttribute("blacklisted", blacklisted);
        model.addAttribute("applied", applied);
        model.addAttribute("saved", saved);
        return "job";
    }

    @RequestMapping("candidate/apply/{jobId}")
    public String apply(@PathVariable long jobId, @RequestParam String coverLetter, Principal principal) {
        Job job = jobService.findById(jobId);
        User user = userService.findByUsername(principal.getName());
        applicationService.save(new Application(job, user, coverLetter));
        savedJobService.deleteByJobAndUser(job, user);
        return "redirect:/job/" + jobId + "?applied";
    }

    @RequestMapping("candidate/save/{jobId}")
    public String saveJob(@PathVariable long jobId, @RequestParam String notes, Principal principal) {
        savedJobService.save(new SavedJob(jobService.findById(jobId), userService.findByUsername(principal.getName()), notes));
        return "redirect:/job/" + jobId + "?saved";
    }

    @RequestMapping("candidate/unsave/{jobId}")
    public String unsaveJob(@PathVariable long jobId, Principal principal) {
        savedJobService.deleteByJobAndUser(jobService.findById(jobId), userService.findByUsername(principal.getName()));
        return "redirect:/candidate/jobs";
    }

    @RequestMapping("recruiter/applications/{jobId}")
    public String getApplicationsOfJob(@PathVariable long jobId, Model model, Principal principal) {
        Job job = jobService.findById(jobId);
        model.addAttribute("applications", job.getUser().getId() == userService.findByUsername(principal.getName()).getId() ? job.getApplications() : new ArrayList<>());
        return "applications";
    }

    @RequestMapping("recruiter/profiles/{jobId}")
    public String getProfilesOfJob(@PathVariable long jobId, Model model, Principal principal) {
        ArrayList<User> profiles = new ArrayList<>();
        for (Application application : jobService.findById(jobId).getApplications()) {
            User user = application.getUser();
            user.setBlacklist(false);
            profiles.add(user);
        }
        model.addAttribute("profiles", markBlacklistedCandidates(profiles, principal));
        model.addAttribute("search", false);
        return "profiles";
    }

    @RequestMapping("profile/{profileId}")
    public String getProfile(@PathVariable long profileId, Model model, Principal principal, Authentication authentication) {
        User candidate = userService.findById(profileId);
        long recruiterId = userService.findByUsername(principal.getName()).getId();
        candidate.setBlacklist(false);
        if (isRecruiter(authentication)) {
            for (Blacklist blacklist : blacklistService.findAllByRecruiterId(recruiterId)) {
                if (blacklist.getCandidateId() == profileId) {
                    candidate.setBlacklist(true);
                    candidate.setBlreason(blacklist.getReason());
                    break;
                }
            }
        }
        model.addAttribute("profile", candidate);
        model.addAttribute("candidate", candidate.getId() == recruiterId);
        return "profile";
    }

    @RequestMapping("recruiter/blacklist/{profileId}")
    public String blacklist(@PathVariable long profileId, @RequestParam String reason, Principal principal) {
        User candidate = userService.findById(profileId);
        User recruiter = userService.findByUsername(principal.getName());
        for (Job job : recruiter.getCreatedJobs()) {
            applicationService.deleteByJobAndUser(job, candidate);
            savedJobService.deleteByJobAndUser(job, candidate);
        }
        blacklistService.save(new Blacklist(candidate.getId(), recruiter.getId(), reason));
        return "redirect:/profile/" + profileId;
    }

    @RequestMapping("recruiter/whitelist/{profileId}")
    public String whitelist(@PathVariable long profileId, Principal principal) {
        blacklistService.deleteByCandidateIdAndRecruiterId(userService.findById(profileId).getId(), userService.findByUsername(principal.getName()).getId());
        return "redirect:/profile/" + profileId;
    }

    @RequestMapping("application/{applicationId}")
    public String getApplication(@PathVariable long applicationId, Model model, Principal principal, Authentication authentication) {
        Application application = applicationService.findById(applicationId);
        User user = userService.findByUsername(principal.getName());
        if (!(application.getUser().getId() == user.getId() || (isRecruiter(authentication) && application.getJob().getUser().getId() == user.getId()))) {
            return "redirect:/";
        }
        model.addAttribute("app", application);
        return "application";
    }

    @RequestMapping("candidate/cancel/{applicationId}")
    public String cancelApplication(@PathVariable long applicationId, Principal principal) {
        if (applicationService.findById(applicationId).getUser().getId() == userService.findByUsername(principal.getName()).getId()) {
            applicationService.deleteById(applicationId);
        }
        return "redirect:/candidate/applications";
    }

    @RequestMapping("recruiter/application/change/{applicationId}")
    public String changeApplicationStatus(@PathVariable long applicationId, @RequestParam int status, Principal principal) {
        Application application = applicationService.findById(applicationId);
        if (application.getJob().getUser().getId() == userService.findByUsername(principal.getName()).getId()) {
            application.setStatus(status);
            applicationService.save(application);
            User candidate = application.getUser();
            Job job = application.getJob();
            String[] statuses = {"waiting", "processing", "accepted", "rejected"};
            String subject = "Your application for " + job.getCompany();
            String message = "Dear " + candidate.getName() + ",\n"
                    + "Your application for the " + job.getPosition() + " is " + statuses[application.getStatus()] + ".\n"
                    + "Thank you for your interest.";
            mailSender.send(candidate.getUsername(), candidate.getName(), subject, message);
        }
        return "redirect:/application/" + applicationId;
    }

    @RequestMapping("candidate/profile")
    public String myProfile(Model model, Principal principal) {
        model.addAttribute("profile", userService.findByUsername(principal.getName()));
        model.addAttribute("candidate", true);
        return "profile";
    }

    @RequestMapping("candidate/profile/edit")
    public String editProfile(Model model, Principal principal) {
        model.addAttribute("profile", userService.findByUsername(principal.getName()));
        return "profile-edit";
    }

    @RequestMapping("candidate/profile/save")
    public String saveProfile(User user, Principal principal) {
        if (user.getId() == userService.findByUsername(principal.getName()).getId()) {
            userService.save(user);
            experienceService.saveAll(user.getExperiences());
            educationService.saveAll(user.getEducations());
        }
        return "redirect:/candidate/profile";
    }

    @RequestMapping("candidate/experience/delete/{experienceId}")
    public String deleteExperience(@PathVariable long experienceId, Principal principal) {
        if (userService.findByUsername(principal.getName()).getExperiences().stream().anyMatch(experience -> experience.getId() == experienceId)) {
            experienceService.deleteById(experienceId);
        }
        return "redirect:/candidate/profile/edit";
    }

    @RequestMapping("candidate/education/delete/{educationId}")
    public String deleteEducation(@PathVariable long educationId, Principal principal) {
        if (userService.findByUsername(principal.getName()).getEducations().stream().anyMatch(education -> education.getId() == educationId)) {
            educationService.deleteById(educationId);
        }
        return "redirect:/candidate/profile/edit";
    }

    @RequestMapping("candidate/experience/add")
    public String addExperience(Principal principal) {
        experienceService.save(new Experience(userService.findByUsername(principal.getName()), "", "", ""));
        return "redirect:/candidate/profile/edit";
    }

    @RequestMapping("candidate/education/add")
    public String addEducation(Principal principal) {
        educationService.save(new Education(userService.findByUsername(principal.getName()), "", "", "", ""));
        return "redirect:/candidate/profile/edit";
    }
}
