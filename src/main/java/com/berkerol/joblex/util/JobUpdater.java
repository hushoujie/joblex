package com.berkerol.joblex.util;

import com.berkerol.joblex.entities.Job;
import com.berkerol.joblex.services.JobService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JobUpdater {

    private final JobService jobService;

    public JobUpdater(JobService jobService) {
        this.jobService = jobService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void updateJobStatus() {
        Date date = new Date();
        for (Job job : jobService.findAll()) {
            boolean change = false;
            if (job.getActivation() != null && job.getActivation().before(date)) {
                job.setStatus(true);
                change = true;
            }
            if (job.getDeactivation() != null && job.getDeactivation().before(date)) {
                job.setStatus(false);
                change = true;
            }
            if (change) {
                jobService.save(job);
            }
        }
    }
}
