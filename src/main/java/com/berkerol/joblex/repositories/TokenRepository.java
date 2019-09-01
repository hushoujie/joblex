package com.berkerol.joblex.repositories;

import com.berkerol.joblex.entities.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TokenRepository extends CrudRepository<Token, Long> {

    Token findByToken(String token);
}
