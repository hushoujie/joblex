package main.services;

import main.domains.Ilan;
import main.repositories.IlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IlanService {

    private IlanRepository ilanRepository;

    @Autowired
    public void setIlanRepository(IlanRepository ilanRepository) {
        this.ilanRepository = ilanRepository;
    }

    public Iterable<Ilan> tumIlanlar() {
        return ilanRepository.findAll();
    }

    public Iterable<Ilan> tumIlanlar(String uzman) {
        return ilanRepository.findAllByUzman(uzman);
    }

    public Ilan ilanBul(int kod) {
        return ilanRepository.findOne(kod);
    }

    public Ilan ilanKaydet(Ilan ilan) {
        return ilanRepository.save(ilan);
    }

    public void ilanSil(int kod) {
        ilanRepository.delete(kod);
    }

}
