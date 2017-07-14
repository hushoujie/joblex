package main.services;

import main.entities.Basvuru;
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

    public Basvuru basvuruBul(Integer id) {
        return basvuruRepository.findOne(id);
    }

    public Basvuru basvuruKaydet(Basvuru basvuru) {
        return basvuruRepository.save(basvuru);
    }

    public void basvuruSil(Integer id) {
        basvuruRepository.delete(id);
    }

}
