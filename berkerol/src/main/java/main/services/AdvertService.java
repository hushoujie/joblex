package main.services;

import main.entities.Advert;
import main.repositories.AdvertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertService {

    private AdvertRepository advertRepository;

    @Autowired
    public void setAdvertRepository(AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }

    public Iterable<Advert> findAllAdverts() {
        return advertRepository.findAll();
    }

    public Advert findAdvert(int id) {
        return advertRepository.findOne(id);
    }

    public Advert saveAdvert(Advert advert) {
        return advertRepository.save(advert);
    }

    public void deleteAdvert(int id) {
        advertRepository.delete(id);
    }

}
