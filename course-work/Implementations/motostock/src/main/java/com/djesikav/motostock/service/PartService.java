package com.djesikav.motostock.service;

import com.djesikav.motostock.entity.Category;
import com.djesikav.motostock.entity.Part;
import com.djesikav.motostock.entity.Supplier;
import com.djesikav.motostock.repository.CategoryRepository;
import com.djesikav.motostock.repository.PartRepository;
import com.djesikav.motostock.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return partRepository.findById(id).orElse(null);
    }

    public Page<Part> findAll(Pageable pageable) {
        return partRepository.findAll(pageable);
    }

    @Transactional
    public Part create(Part part, Long categoryId, Long supplierId) {
        Category category =  categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category not found with id: " + categoryId));
        Supplier supplier =  supplierRepository.findById(supplierId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category not found with id: " + supplierId));

        if (part.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        if (part.getPurchasePrice() == null || part.getPurchasePrice().signum() < 0) {
            throw new IllegalArgumentException("Purchase price cannot be negative");
        }

        if (part.getSellingPriceFormat() == null || part.getSellingPriceFormat().signum() < 0) {
            throw new IllegalArgumentException("Selling price cannot be negative");
        }

        part.setCategory(category);
        part.setSupplier(supplier);

        return partRepository.save(part);
    }

    @Transactional
    public Part update(Long id, Part part) {
        Part partToUpdate = findById(id);

        partToUpdate.setName(part.getName());
        partToUpdate.setCategory(part.getCategory());
        partToUpdate.setBrand(part.getBrand());
        partToUpdate.setQuantity(part.getQuantity());
        partToUpdate.setSku(part.getSku());
        partToUpdate.setPurchasePrice(part.getPurchasePrice());
        partToUpdate.setSellingPriceFormat(part.getSellingPriceFormat());
        partToUpdate.setSupplier(part.getSupplier());

        return partRepository.save(partToUpdate);
    }

    @Transactional
    public void delete(Long id) {
        Part partToDelete = findById(id);
        partRepository.delete(partToDelete);
    }
}
