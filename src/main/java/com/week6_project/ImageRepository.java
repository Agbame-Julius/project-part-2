package com.week6_project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Page<Image> findAllByOrderByIdDesc(Pageable pageable);

    Optional<Image> findByUrl(String url);

}
