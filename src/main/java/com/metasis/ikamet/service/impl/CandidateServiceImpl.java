package com.metasis.ikamet.service.impl;

import com.metasis.ikamet.domain.Candidate;
import com.metasis.ikamet.repository.CandidateRepository;
import com.metasis.ikamet.service.CandidateService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Candidate}.
 */
@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {

    private final Logger log = LoggerFactory.getLogger(CandidateServiceImpl.class);

    private final CandidateRepository candidateRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Override
    public Mono<Candidate> save(Candidate candidate) {
        log.debug("Request to save Candidate : {}", candidate);
        return candidateRepository.save(candidate);
    }

    @Override
    public Mono<Candidate> partialUpdate(Candidate candidate) {
        log.debug("Request to partially update Candidate : {}", candidate);

        return candidateRepository
            .findById(candidate.getId())
            .map(existingCandidate -> {
                if (candidate.getFirstName() != null) {
                    existingCandidate.setFirstName(candidate.getFirstName());
                }
                if (candidate.getLastName() != null) {
                    existingCandidate.setLastName(candidate.getLastName());
                }
                if (candidate.getUniversity() != null) {
                    existingCandidate.setUniversity(candidate.getUniversity());
                }
                if (candidate.getGraduationYear() != null) {
                    existingCandidate.setGraduationYear(candidate.getGraduationYear());
                }
                if (candidate.getGpa() != null) {
                    existingCandidate.setGpa(candidate.getGpa());
                }
                if (candidate.getEditBy() != null) {
                    existingCandidate.setEditBy(candidate.getEditBy());
                }

                return existingCandidate;
            })
            .flatMap(candidateRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Candidate> findAll() {
        log.debug("Request to get all Candidates");
        return candidateRepository.findAll();
    }

    public Mono<Long> countAll() {
        return candidateRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Candidate> findOne(Long id) {
        log.debug("Request to get Candidate : {}", id);
        return candidateRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Candidate : {}", id);
        return candidateRepository.deleteById(id);
    }
}
