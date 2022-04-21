package com.metasis.ikamet.service.impl;

import com.metasis.ikamet.domain.File;
import com.metasis.ikamet.repository.FileRepository;
import com.metasis.ikamet.service.FileService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link File}.
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

    private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public Mono<File> save(File file) {
        log.debug("Request to save File : {}", file);
        return fileRepository.save(file);
    }

    @Override
    public Mono<File> partialUpdate(File file) {
        log.debug("Request to partially update File : {}", file);

        return fileRepository
            .findById(file.getId())
            .map(existingFile -> {
                if (file.getName() != null) {
                    existingFile.setName(file.getName());
                }
                if (file.getData() != null) {
                    existingFile.setData(file.getData());
                }
                if (file.getDataContentType() != null) {
                    existingFile.setDataContentType(file.getDataContentType());
                }

                return existingFile;
            })
            .flatMap(fileRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<File> findAll() {
        log.debug("Request to get all Files");
        return fileRepository.findAll();
    }

    public Mono<Long> countAll() {
        return fileRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<File> findOne(Long id) {
        log.debug("Request to get File : {}", id);
        return fileRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete File : {}", id);
        return fileRepository.deleteById(id);
    }
}
