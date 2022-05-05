package com.my.thesis.repository;

import com.my.thesis.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDbRepository extends JpaRepository<Image, Long> {}
