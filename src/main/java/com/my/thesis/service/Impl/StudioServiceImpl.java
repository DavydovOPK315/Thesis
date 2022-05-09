package com.my.thesis.service.Impl;

import com.my.thesis.model.Studio;
import com.my.thesis.repository.StudioRepository;
import com.my.thesis.service.StudioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StudioServiceImpl implements StudioService {

    private final StudioRepository studioRepository;

    @Autowired
    public StudioServiceImpl(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    @Override
    public Studio findByName(String name) {

        Studio result = studioRepository.findByName(name);

        if (result == null) {
            log.warn("IN findByName - no studio found {}", name);
            return null;
        }

        log.info("IN findByName - studio: {} successfully found by name", result.getName());
        return result;
    }

    @Override
    public Studio findById(Long id) {
        return studioRepository.findById(id).orElse(null);
    }

    @Override
    public List<Studio> getAll() {

        List<Studio> result = studioRepository.findAll();

        if (result.isEmpty()) {
            log.warn("In getAll - no studio found");
            return null;
        }

        return result;
    }
}
