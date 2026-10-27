package com.djesikav.motostock.service;

import com.djesikav.motostock.entity.Category;
import com.djesikav.motostock.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Long id, Category category) {
        Category categoryToUpdate = findById(id);

        categoryToUpdate.setName(category.getName());
        categoryToUpdate.setDescription(category.getDescription());
        categoryToUpdate.setDisplayOrder(category.getDisplayOrder());
        categoryToUpdate.setActive(category.getActive());

        return categoryRepository.save(categoryToUpdate);
    }

    @Transactional
    public void delete(Long id) {
        Category categoryToDelete = findById(id);
        categoryRepository.delete(categoryToDelete);
    }
}
