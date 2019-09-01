package com.berkerol.joblex.repositories;

import com.berkerol.joblex.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
