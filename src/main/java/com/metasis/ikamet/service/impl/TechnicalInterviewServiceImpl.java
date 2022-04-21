package com.metasis.ikamet.service.impl;

import com.metasis.ikamet.domain.TechnicalInterview;
import com.metasis.ikamet.repository.TechnicalInterviewRepository;
import com.metasis.ikamet.service.TechnicalInterviewService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TechnicalInterview}.
 */
@Service
@Transactional
public class TechnicalInterviewServiceImpl implements TechnicalInterviewService {

    private final Logger log = LoggerFactory.getLogger(TechnicalInterviewServiceImpl.class);

    private final TechnicalInterviewRepository technicalInterviewRepository;

    public TechnicalInterviewServiceImpl(TechnicalInterviewRepository technicalInterviewRepository) {
        this.technicalInterviewRepository = technicalInterviewRepository;
    }

    @Override
    public Mono<TechnicalInterview> save(TechnicalInterview technicalInterview) {
        log.debug("Request to save TechnicalInterview : {}", technicalInterview);
        return technicalInterviewRepository.save(technicalInterview);
    }

    @Override
    public Mono<TechnicalInterview> partialUpdate(TechnicalInterview technicalInterview) {
        log.debug("Request to partially update TechnicalInterview : {}", technicalInterview);

        return technicalInterviewRepository
            .findById(technicalInterview.getId())
            .map(existingTechnicalInterview -> {
                if (technicalInterview.getDate() != null) {
                    existingTechnicalInterview.setDate(technicalInterview.getDate());
                }
                if (technicalInterview.getScore() != null) {
                    existingTechnicalInterview.setScore(technicalInterview.getScore());
                }
                if (technicalInterview.getTechnicalStatus() != null) {
                    existingTechnicalInterview.setTechnicalStatus(technicalInterview.getTechnicalStatus());
                }
                if (technicalInterview.getNotes() != null) {
                    existingTechnicalInterview.setNotes(technicalInterview.getNotes());
                }
                if (technicalInterview.getEditBy() != null) {
                    existingTechnicalInterview.setEditBy(technicalInterview.getEditBy());
                }

                return existingTechnicalInterview;
            })
            .flatMap(technicalInterviewRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TechnicalInterview> findAll() {
        log.debug("Request to get all TechnicalInterviews");
        return technicalInterviewRepository.findAll();
    }

    public Mono<Long> countAll() {
        return technicalInterviewRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<TechnicalInterview> findOne(Long id) {
        log.debug("Request to get TechnicalInterview : {}", id);
        return technicalInterviewRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TechnicalInterview : {}", id);
        return technicalInterviewRepository.deleteById(id);
    }
}
