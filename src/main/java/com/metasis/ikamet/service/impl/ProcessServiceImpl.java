package com.metasis.ikamet.service.impl;

import com.metasis.ikamet.domain.Process;
import com.metasis.ikamet.repository.ProcessRepository;
import com.metasis.ikamet.service.ProcessService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Process}.
 */
@Service
@Transactional
public class ProcessServiceImpl implements ProcessService {

    private final Logger log = LoggerFactory.getLogger(ProcessServiceImpl.class);

    private final ProcessRepository processRepository;

    public ProcessServiceImpl(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    @Override
    public Mono<Process> save(Process process) {
        log.debug("Request to save Process : {}", process);
        return processRepository.save(process);
    }

    @Override
    public Mono<Process> partialUpdate(Process process) {
        log.debug("Request to partially update Process : {}", process);

        return processRepository
            .findById(process.getId())
            .map(existingProcess -> {
                if (process.getPdate() != null) {
                    existingProcess.setPdate(process.getPdate());
                }
                if (process.getDepartment() != null) {
                    existingProcess.setDepartment(process.getDepartment());
                }
                if (process.getTechnicalIndicators() != null) {
                    existingProcess.setTechnicalIndicators(process.getTechnicalIndicators());
                }
                if (process.getExperience() != null) {
                    existingProcess.setExperience(process.getExperience());
                }
                if (process.getPosition() != null) {
                    existingProcess.setPosition(process.getPosition());
                }
                if (process.getSalaryExpectation() != null) {
                    existingProcess.setSalaryExpectation(process.getSalaryExpectation());
                }
                if (process.getPossibleAssignmnet() != null) {
                    existingProcess.setPossibleAssignmnet(process.getPossibleAssignmnet());
                }
                if (process.getLastStatus() != null) {
                    existingProcess.setLastStatus(process.getLastStatus());
                }
                if (process.getLastDescription() != null) {
                    existingProcess.setLastDescription(process.getLastDescription());
                }
                if (process.getEditBy() != null) {
                    existingProcess.setEditBy(process.getEditBy());
                }

                return existingProcess;
            })
            .flatMap(processRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Process> findAll() {
        log.debug("Request to get all Processes");
        return processRepository.findAll();
    }

    public Mono<Long> countAll() {
        return processRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Process> findOne(Long id) {
        log.debug("Request to get Process : {}", id);
        return processRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Process : {}", id);
        return processRepository.deleteById(id);
    }
}
