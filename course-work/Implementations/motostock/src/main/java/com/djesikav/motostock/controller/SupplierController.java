package com.djesikav.motostock.controller;

import com.djesikav.motostock.dto.form.SupplierForm;
import com.djesikav.motostock.entity.Supplier;
import com.djesikav.motostock.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public String list(Model model){
        Page<Supplier> suppliers = supplierService.findAll(
                PageRequest.of(0, 10, Sort.by("name").ascending())
        );

        model.addAttribute("suppliers", suppliers.getContent());
        model.addAttribute("supplierPage", suppliers);

        return "/suppliers/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("supplier", new SupplierForm());

        return "suppliers/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@Valid @ModelAttribute("supplierForm") SupplierForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "suppliers/form";
        }

        supplierService.create(form);

        return "redirect:/suppliers";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        Supplier supplier = supplierService.findById(id);

        SupplierForm form = new SupplierForm();

        form.setName(supplier.getName());
        form.setEmail(supplier.getEmail());
        form.setPriority(supplier.getPriority());
        form.setActive(supplier.isActive());

        model.addAttribute("supplier", form);
        model.addAttribute("supplierID", id);

        return "suppliers/form";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("supplierForm") SupplierForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("supplier", id);
            return "suppliers/form";
        }

        supplierService.update(id, form);

        return "redirect:/suppliers";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            supplierService.delete(id);

            redirectAttributes.addFlashAttribute("successMessage", "Supplier deleted successfully");
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete this supplier because it is assigned to existing parts.");
        }

        return "redirect:/suppliers";
    }
}
