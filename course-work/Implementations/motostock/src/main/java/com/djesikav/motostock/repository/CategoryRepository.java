package com.djesikav.motostock.repository;

import com.djesikav.motostock.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
           SELECT c FROM Category c
           WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))
           AND (:active IS NULL OR c.active = :active)
           """)
    Page<Category> search(
            @Param("name") String name,
            @Param("active") Boolean active,
            Pageable pageable
    );
}
