package com.djesikav.motostock.service;

import com.djesikav.motostock.dto.form.SupplierForm;
import com.djesikav.motostock.entity.Supplier;
import com.djesikav.motostock.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public Supplier create(SupplierForm form) {

        Supplier supplier = new Supplier();

        supplier.setName(form.getName());
        supplier.setEmail(form.getEmail());
        supplier.setPriority(form.getPriority());
        supplier.setActive(form.getActive());

        return supplierRepository.save(supplier);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Supplier update(Long id, SupplierForm form) {
        Supplier supplierToUpdate = findById(id);

        supplierToUpdate.setName(form.getName());
        supplierToUpdate.setEmail(form.getEmail());
        supplierToUpdate.setActive(form.getActive());
        supplierToUpdate.setPriority(form.getPriority());

        return supplierRepository.save(supplierToUpdate);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        Supplier supplierToDelete = findById(id);
        supplierRepository.delete(supplierToDelete);
    }

    @PreAuthorize("isAuthenticated()")
    public Page<Supplier> search(
            String name,
            String email,
            Pageable pageable) {

        String searchName = name == null ? "" : name.trim();
        String searchEmail = email == null ? "" : email.trim();

        if (searchEmail.isEmpty()) {
            return supplierRepository.findByNameContainingIgnoreCase(
                    searchName,
                    pageable
            );
        }

        return supplierRepository
                .findByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(
                        searchName,
                        searchEmail,
                        pageable
                );
    }
}
