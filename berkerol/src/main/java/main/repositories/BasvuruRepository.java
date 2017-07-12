package main.repositories;

import main.domains.Basvuru;
import org.springframework.data.repository.CrudRepository;

public interface BasvuruRepository extends CrudRepository<Basvuru, Integer> {

    public Iterable<Basvuru> findAllByAday(String aday);

    public Iterable<Basvuru> findAllByIlan(int ilan);

}
