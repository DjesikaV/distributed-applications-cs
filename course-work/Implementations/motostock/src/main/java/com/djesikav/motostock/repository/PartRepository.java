package com.djesikav.motostock.repository;

import com.djesikav.motostock.entity.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {

    @Override
    @EntityGraph(attributePaths = {"category", "supplier"})
    Page<Part> findAll(Pageable pageable);
}
