package main.services;

import main.domains.Pozisyon;
import main.repositories.PozisyonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PozisyonService {

    private PozisyonRepository pozisyonRepository;

    @Autowired
    public void setPozisyonRepository(PozisyonRepository pozisyonRepository) {
        this.pozisyonRepository = pozisyonRepository;
    }

    public Iterable<Pozisyon> tumPozisyonlar() {
        return pozisyonRepository.findAll();
    }

    public Iterable<Pozisyon> tumPozisyonlar(String aday) {
        return pozisyonRepository.findAllByAday(aday);
    }

    public Pozisyon pozisyonBul(String id) {
        return pozisyonRepository.findOne(id);
    }

    public Pozisyon pozisyonKaydet(Pozisyon pozisyon) {
        return pozisyonRepository.save(pozisyon);
    }

    public void pozisyonSil(String id) {
        pozisyonRepository.delete(id);
    }

}
