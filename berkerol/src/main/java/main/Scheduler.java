package main;

import java.util.Date;
import main.entities.Advert;
import main.services.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private AdvertService advertService;

    @Autowired
    public void setAdvertService(AdvertService advertService) {
        this.advertService = advertService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void setAdvertStatuses() {
        Date date = new Date();
        for (Advert advert : advertService.findAllAdverts()) {
            boolean change = false;
            if (advert.getActivation().before(date)) {
                advert.setStatus(true);
                change = true;
            }
            if (advert.getDeactivation().before(date)) {
                advert.setStatus(false);
                change = true;
            }
            if (change) {
                advertService.saveAdvert(advert);
            }
        }

    }
}
