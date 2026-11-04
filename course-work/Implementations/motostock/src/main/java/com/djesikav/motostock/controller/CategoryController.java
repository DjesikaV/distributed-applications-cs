package com.djesikav.motostock.controller;

import com.djesikav.motostock.dto.form.CategoryForm;
import com.djesikav.motostock.entity.Category;
import com.djesikav.motostock.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
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
        var categories = categoryService.findAll(
                PageRequest.of(0, 10, Sort.by("name").ascending())
        );

        model.addAttribute("categories", categories.getContent());

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


}
