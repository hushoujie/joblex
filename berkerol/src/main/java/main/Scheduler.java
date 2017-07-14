package main;

import java.util.Date;
import main.entities.Ilan;
import main.services.IlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private IlanService ilanService;

    @Autowired
    public void setIlanService(IlanService ilanService) {
        this.ilanService = ilanService;
    }

    @Scheduled(cron = "0/10 * * * * *")
    public void ilanDurumlari() {
        System.out.println("Ilan Durum Kontrolu");
        Date date = new Date();
        for (Ilan ilan : ilanService.tumIlanlar()) {
            if (ilan.getAktivasyon().before(date)) {
                ilan.setDurum(true);
            }
            if (ilan.getKapanma().before(date)) {
                ilan.setDurum(false);
            }
            ilanService.ilanKaydet(ilan);
        }

    }
}
