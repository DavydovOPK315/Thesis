package com.my.thesis.service.Impl;

import com.my.thesis.model.Os;
import com.my.thesis.repository.OsRepository;
import com.my.thesis.service.OsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OsServiceImpl implements OsService {

    private final OsRepository osRepository;

    @Autowired
    public OsServiceImpl(OsRepository osRepository) {
        this.osRepository = osRepository;
    }

    @Override
    public Os findByName(String name) {
        Os result = osRepository.findByName(name);

        if (result == null) {
            log.warn("IN findByName - no os found {}", name);
            return null;
        }

        log.info("IN findByName - os: {} successfully found by name", result.getName());
        return result;
    }

    @Override
    public Os findById(Long id) {
        return osRepository.findById(id).orElse(null);
    }

    @Override
    public List<Os> getAll() {
        List<Os> result = osRepository.findAll();

        if (result.isEmpty()) {
            log.warn("IN getAll - no os found");
            return null;
        }

        log.info("IN getAll oss were found {}", result.size());

        return result;
    }
}
