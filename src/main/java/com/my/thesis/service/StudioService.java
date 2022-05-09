package com.my.thesis.service;

import com.my.thesis.model.Studio;

import java.util.List;

public interface StudioService {
    Studio findByName(String name);
    Studio findById(Long id);
    List<Studio> getAll();
}
