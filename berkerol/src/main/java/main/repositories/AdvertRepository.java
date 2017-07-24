package main.repositories;

import java.util.LinkedList;
import main.entities.Advert;
import org.springframework.data.repository.CrudRepository;

public interface AdvertRepository extends CrudRepository<Advert, Integer> {

    @Override
    public LinkedList<Advert> findAll();

    public LinkedList<Advert> findAllByStatus(boolean status);

}
