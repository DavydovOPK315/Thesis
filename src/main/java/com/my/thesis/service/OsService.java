package com.my.thesis.service;

import com.my.thesis.model.Os;

import java.util.List;

public interface OsService {
    Os findByName(String name);
    Os findById(Long id);
    List<Os> getAll();
}
