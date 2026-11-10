package com.djesikav.motostock.controller;

import com.djesikav.motostock.dto.form.PartForm;
import com.djesikav.motostock.entity.Part;
import com.djesikav.motostock.service.CategoryService;
import com.djesikav.motostock.service.PartService;
import com.djesikav.motostock.service.SupplierService;
import jakarta.validation.Valid;
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
@RequestMapping("/parts")
public class PartController {

    private final PartService partService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    public PartController(PartService partService,  CategoryService categoryService, SupplierService supplierService) {
        this.partService = partService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
    }

    @GetMapping
    public String list(Model model) {
        Page<Part> parts = partService.findAll(
                PageRequest.of(0, 10, Sort.by("name").ascending())
        );

        model.addAttribute("parts", parts.getContent());
        model.addAttribute("partPage", parts);

        return "parts/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        PartForm form = new PartForm();
        form.setQuantity(0);

        model.addAttribute("partForm", form);
        loadDropdowns(model);

        return "parts/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@Valid @ModelAttribute("partForm") PartForm form,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            loadDropdowns(model);
            return "parts/form";
        }

        partService.create(form);

        return "redirect:/parts";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {

        Part part = partService.findById(id);

        PartForm form = new PartForm();

        form.setSku(part.getSku());
        form.setName(part.getName());
        form.setBrand(part.getBrand());
        form.setCategoryId(part.getCategory().getId());
        form.setSupplierId(part.getSupplier().getId());
        form.setQuantity(part.getQuantity());
        form.setPurchasePrice(part.getPurchasePrice());
        form.setSellingPrice(part.getSellingPrice());

        model.addAttribute("partForm", form);
        model.addAttribute("partId", id);

        loadDropdowns(model);

        return "parts/form";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("partForm") PartForm form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("partId", id);
            loadDropdowns(model);

            return "parts/form";
        }

        partService.update(id, form);

        return "redirect:/parts";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        partService.delete(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Part deleted successfully."
        );

        return "redirect:/parts";
    }

    private void loadDropdowns(Model model) {

        model.addAttribute("categories",
                categoryService.findAll(
                        Pageable.unpaged(Sort.by("name"))
                ).getContent());

        model.addAttribute("suppliers",
                supplierService.findAll(
                        Pageable.unpaged(Sort.by("name"))
                ).getContent());
    }
}
