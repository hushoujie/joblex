package main.repositories;

import main.domains.Pozisyon;
import org.springframework.data.repository.CrudRepository;

public interface PozisyonRepository extends CrudRepository<Pozisyon, String> {

    public Iterable<Pozisyon> findAllByAday(String aday);

}
