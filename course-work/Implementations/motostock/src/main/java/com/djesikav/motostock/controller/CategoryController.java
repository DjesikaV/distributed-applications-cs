package com.djesikav.motostock.controller;

import com.djesikav.motostock.dto.form.CategoryForm;
import com.djesikav.motostock.entity.Category;
import com.djesikav.motostock.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        Page<Category> categories = categoryService.findAll(
                PageRequest.of(0, 10, Sort.by("name").ascending())
        );

        model.addAttribute("categories", categories.getContent());
        model.addAttribute("categoryPage", categories);

        return "categories/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "categories/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@Valid CategoryForm categoryForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "categories/form";
        }

        categoryService.create(categoryForm);
        return "redirect:/categories";
    }


    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {

        Category category = categoryService.findById(id);

        CategoryForm form = new CategoryForm();

        form.setName(category.getName());
        form.setDescription(category.getDescription());
        form.setDisplayOrder(category.getDisplayOrder());
        form.setActive(category.getActive());

        model.addAttribute("categoryForm", form);
        model.addAttribute("categoryId", id);

        return "categories/form";
    }


    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("categoryForm") CategoryForm form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryId", id);
            return "categories/form";
        }

        categoryService.update(id, form);

        return "redirect:/categories";
    }


    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {

        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Category deleted successfully.");
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Cannot delete this category because it is used by existing parts.");
        }

        return "redirect:/categories";
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public String search(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "all") String active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {
        Boolean activeFilter = switch (active) {
            case "true" -> true;
            case "false" -> false;
            default -> null;
        };

        String sortField = switch (sort) {
            case "name", "displayOrder", "active", "createdAt" -> sort;
            default -> "name";
        };

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                Math.max(0, page),
                Math.min(Math.max(1, size), 50),
                Sort.by(sortDirection, sortField)
        );

        Page<Category> categories = categoryService.search(name, activeFilter, pageable);

        model.addAttribute("categories", categories.getContent());
        model.addAttribute("categoryPage", categories);

        model.addAttribute("name", name);
        model.addAttribute("active", active);
        model.addAttribute("sort", sortField);
        model.addAttribute("direction", sortDirection.name().toLowerCase());
        model.addAttribute("size", pageable.getPageSize());

        return "categories/list";
    }


}
