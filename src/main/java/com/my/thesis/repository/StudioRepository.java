package com.my.thesis.repository;

import com.my.thesis.model.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudioRepository extends JpaRepository<Studio, Long> {
    Studio findByName(String name);
    Long findIdByName(String name);
}