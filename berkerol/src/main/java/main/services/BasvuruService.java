package main.services;

import main.domains.Basvuru;
import main.repositories.BasvuruRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasvuruService {

    private BasvuruRepository basvuruRepository;

    @Autowired
    public void setBasvuruRepository(BasvuruRepository basvuruRepository) {
        this.basvuruRepository = basvuruRepository;
    }

    public Iterable<Basvuru> tumBasvurular() {
        return basvuruRepository.findAll();
    }

    public Iterable<Basvuru> tumBasvurular(String aday) {
        return basvuruRepository.findAllByAday(aday);
    }

    public Iterable<Basvuru> tumBasvurular(int ilan) {
        return basvuruRepository.findAllByIlan(ilan);
    }

    public Basvuru basvuruBul(int id) {
        return basvuruRepository.findOne(id);
    }

    public Basvuru basvuruKaydet(Basvuru basvuru) {
        return basvuruRepository.save(basvuru);
    }

    public void basvuruSil(int id) {
        basvuruRepository.delete(id);
    }
}
