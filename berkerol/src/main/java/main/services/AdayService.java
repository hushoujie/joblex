package main.services;

import main.domains.Aday;
import main.repositories.AdayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdayService {

    private AdayRepository adayRepository;

    @Autowired
    public void setAdayRepository(AdayRepository adayRepository) {
        this.adayRepository = adayRepository;
    }

    public Iterable<Aday> tumAdaylar() {
        return adayRepository.findAll();
    }

    public Aday adayBul(String id) {
        return adayRepository.findOne(id);
    }

    public Aday adayKaydet(Aday aday) {
        return adayRepository.save(aday);
    }

    public void adaySil(String id) {
        adayRepository.delete(id);
    }

}