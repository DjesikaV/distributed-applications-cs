package com.djesikav.motostock.service;

import com.djesikav.motostock.dto.form.PartForm;
import com.djesikav.motostock.entity.Category;
import com.djesikav.motostock.entity.Part;
import com.djesikav.motostock.entity.Supplier;
import com.djesikav.motostock.repository.CategoryRepository;
import com.djesikav.motostock.repository.PartRepository;
import com.djesikav.motostock.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PartService {

    private final PartRepository partRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public PartService(PartRepository partRepository,  CategoryRepository categoryRepository, SupplierRepository supplierRepository) {
        this.partRepository = partRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    public Part findById(Long id) {
        return partRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Part not found with id: " + id));
    }

    public Page<Part> findAll(Pageable pageable) {
        return partRepository.findAll(pageable);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Part create(PartForm form) {

        Part part = new Part();

        applyForm(part, form);

        return partRepository.save(part);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Part update(Long id, PartForm form) {
        Part partToUpdate = findById(id);

        applyForm(partToUpdate, form);

        return partRepository.save(partToUpdate);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        Part partToDelete = findById(id);
        partRepository.delete(partToDelete);
    }

    private void applyForm(Part part, PartForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Part data is required.");
        }

        if (form.getCategoryId() == null || form.getSupplierId() == null) {
            throw new IllegalArgumentException("Category and supplier data is required.");
        }

        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + form.getCategoryId()));

        Supplier supplier = supplierRepository.findById(form.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + form.getSupplierId()));

        if (form.getQuantity() == null || form.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative or null.");
        }

        if (form.getPurchasePrice() == null || form.getPurchasePrice().signum() < 0) {
            throw new IllegalArgumentException("Purchase price cannot be negative or null.");
        }

        if (form.getSellingPrice() == null || form.getSellingPrice().signum() < 0) {
            throw new IllegalArgumentException("Selling price cannot be negative or null.");
        }

        part.setSku(form.getSku());
        part.setName(form.getName());
        part.setBrand(form.getBrand());
        part.setCategory(category);
        part.setSupplier(supplier);
        part.setQuantity(form.getQuantity());
        part.setPurchasePrice(form.getPurchasePrice());
        part.setSellingPrice(form.getSellingPrice());
    }

    @PreAuthorize("isAuthenticated()")
    public Page<Part> search(String name, String sku, Pageable pageable) {
        String searchName = name == null ? "" : name.trim();
        String searchSku = sku == null ? "" : sku.trim();

        return partRepository.findByNameContainingIgnoreCaseAndSkuContainingIgnoreCase(searchName, searchSku, pageable);
    }
}
