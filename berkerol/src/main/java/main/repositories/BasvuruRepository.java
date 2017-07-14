package main.repositories;

import main.entities.Aday;
import main.entities.Basvuru;
import org.springframework.data.repository.CrudRepository;

public interface BasvuruRepository extends CrudRepository<Basvuru, Integer> {

    public Iterable<Basvuru> findAllByAday(Aday aday);

}
