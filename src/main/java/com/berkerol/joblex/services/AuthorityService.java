package com.berkerol.joblex.services;

import com.berkerol.joblex.entities.Authority;
import com.berkerol.joblex.repositories.AuthorityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public List<Authority> findAllByAuthority(String authority) {
        return authorityRepository.findAllByAuthority(authority);
    }

    public Authority save(Authority authority) {
        return authorityRepository.save(authority);
    }
}
