package com.metasis.ikamet.service.impl;

import com.metasis.ikamet.domain.HrInterview;
import com.metasis.ikamet.repository.HrInterviewRepository;
import com.metasis.ikamet.service.HrInterviewService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link HrInterview}.
 */
@Service
@Transactional
public class HrInterviewServiceImpl implements HrInterviewService {

    private final Logger log = LoggerFactory.getLogger(HrInterviewServiceImpl.class);

    private final HrInterviewRepository hrInterviewRepository;

    public HrInterviewServiceImpl(HrInterviewRepository hrInterviewRepository) {
        this.hrInterviewRepository = hrInterviewRepository;
    }

    @Override
    public Mono<HrInterview> save(HrInterview hrInterview) {
        log.debug("Request to save HrInterview : {}", hrInterview);
        return hrInterviewRepository.save(hrInterview);
    }

    @Override
    public Mono<HrInterview> partialUpdate(HrInterview hrInterview) {
        log.debug("Request to partially update HrInterview : {}", hrInterview);

        return hrInterviewRepository
            .findById(hrInterview.getId())
            .map(existingHrInterview -> {
                if (hrInterview.getDate() != null) {
                    existingHrInterview.setDate(hrInterview.getDate());
                }
                if (hrInterview.getScore() != null) {
                    existingHrInterview.setScore(hrInterview.getScore());
                }
                if (hrInterview.getIkStatus() != null) {
                    existingHrInterview.setIkStatus(hrInterview.getIkStatus());
                }
                if (hrInterview.getNotes() != null) {
                    existingHrInterview.setNotes(hrInterview.getNotes());
                }
                if (hrInterview.getEditBy() != null) {
                    existingHrInterview.setEditBy(hrInterview.getEditBy());
                }

                return existingHrInterview;
            })
            .flatMap(hrInterviewRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HrInterview> findAll() {
        log.debug("Request to get all HrInterviews");
        return hrInterviewRepository.findAll();
    }

    public Mono<Long> countAll() {
        return hrInterviewRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<HrInterview> findOne(Long id) {
        log.debug("Request to get HrInterview : {}", id);
        return hrInterviewRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete HrInterview : {}", id);
        return hrInterviewRepository.deleteById(id);
    }
}
