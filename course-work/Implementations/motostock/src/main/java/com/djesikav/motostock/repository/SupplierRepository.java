package com.djesikav.motostock.repository;

import com.djesikav.motostock.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
