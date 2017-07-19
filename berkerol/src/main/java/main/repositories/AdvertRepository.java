package main.repositories;

import main.entities.Advert;
import org.springframework.data.repository.CrudRepository;

public interface AdvertRepository extends CrudRepository<Advert, Integer> {

}
