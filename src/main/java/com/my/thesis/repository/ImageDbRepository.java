package com.my.thesis.repository;

import com.my.thesis.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;

@Repository
public interface ImageDbRepository extends JpaRepository<Image, Long> {
    Image findByContent(@NotEmpty byte[] content);
}
