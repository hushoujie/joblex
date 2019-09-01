package com.berkerol.joblex.services;

import com.berkerol.joblex.entities.Blacklist;
import com.berkerol.joblex.repositories.BlacklistRepository;
import org.springframework.stereotype.Service;

@Service
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;

    public BlacklistService(BlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    public Iterable<Blacklist> findAllByRecruiterId(long recruiterId) {
        return blacklistRepository.findAllByRecruiterId(recruiterId);
    }

    public Blacklist save(Blacklist blacklist) {
        return blacklistRepository.save(blacklist);
    }

    public void deleteByCandidateIdAndRecruiterId(long candidateId, long recruiterId) {
        blacklistRepository.deleteByCandidateIdAndRecruiterId(candidateId, recruiterId);
    }
}
