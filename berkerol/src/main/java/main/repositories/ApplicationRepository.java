package main.repositories;

import main.entities.Application;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<Application, Integer> {

}
