package com.djesikav.motostock.repository;

import com.djesikav.motostock.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
