package com.my.thesis.repository;

import com.my.thesis.model.Os;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OsRepository extends JpaRepository<Os, Long> {
    Os findByName(String name);
    Long findIdByName(String name);
}