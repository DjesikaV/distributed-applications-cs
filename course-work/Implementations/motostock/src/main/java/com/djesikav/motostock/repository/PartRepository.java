package com.djesikav.motostock.repository;

import com.djesikav.motostock.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {
}
