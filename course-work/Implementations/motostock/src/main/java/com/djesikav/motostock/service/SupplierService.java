package com.djesikav.motostock.service;

import com.djesikav.motostock.entity.Supplier;
import com.djesikav.motostock.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Page<Supplier> findAll(Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }

    public Supplier findById(long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Supplier not found with id " + id));
    }

    @Transactional
    public Supplier create(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Transactional
    public Supplier update(Long id, Supplier supplier) {
        Supplier supplierToUpdate = findById(id);

        supplierToUpdate.setName(supplier.getName());
        supplierToUpdate.setActive(supplier.isActive());
        supplierToUpdate.setEmail(supplier.getEmail());
        supplierToUpdate.setPriority(supplier.getPriority());

        return supplierRepository.save(supplierToUpdate);
    }

    @Transactional
    public void delete(Long id) {
        Supplier supplierToDelete = findById(id);
        supplierRepository.delete(supplierToDelete);
    }
}
