package com.djesikav.motostock.service;

import com.djesikav.motostock.dto.form.CategoryForm;
import com.djesikav.motostock.entity.Category;
import com.djesikav.motostock.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category not found with id: " + id));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Category create(CategoryForm form) {
        Category category = new Category();

        category.setName(form.getName());
        category.setDescription(form.getDescription());
        category.setDisplayOrder(form.getDisplayOrder());
        category.setActive(form.getActive());

        return categoryRepository.save(category);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Category update(Long id, CategoryForm form) {
        Category categoryToUpdate = findById(id);

        categoryToUpdate.setName(form.getName());
        categoryToUpdate.setDescription(form.getDescription());
        categoryToUpdate.setDisplayOrder(form.getDisplayOrder());
        categoryToUpdate.setActive(form.getActive());

        return categoryRepository.save(categoryToUpdate);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        Category categoryToDelete = findById(id);
        categoryRepository.delete(categoryToDelete);
    }

    @PreAuthorize("isAuthenticated()")
    public Page<Category> search(String name, Boolean active, Pageable pageable) {
        String searchName = name == null ? "" : name.trim();

        return categoryRepository.search(searchName, active, pageable);
    }
}
