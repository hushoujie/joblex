package deneme;

import org.springframework.data.repository.CrudRepository;

public interface IlanRepository extends CrudRepository<Ilan, Integer> {

    public Iterable<Ilan> findAllByUzman(String uzman);

}
