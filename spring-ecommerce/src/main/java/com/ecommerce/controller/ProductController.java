package com.ecommerce.controller;

import com.ecommerce.constants.endpoints.ProductControllerEndpointRoutes;
import com.ecommerce.model.Product;
import com.ecommerce.service.IProductService;

import static com.ecommerce.constants.view.ViewConstants.*;
import static com.ecommerce.constants.redirect.RedirectConstants.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final IProductService iProductService;

    public ProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @GetMapping(ProductControllerEndpointRoutes.SHOW)
    public String show(Model model) {
        model.addAttribute("products", iProductService.findAll());
        return SHOW_VIEW;

    }

    @GetMapping(ProductControllerEndpointRoutes.CREATE)
    public String create() {
        return CREATE_VIEW;
    }

    @PostMapping(ProductControllerEndpointRoutes.SAVE)
    public String save(Product product, @RequestParam("img") MultipartFile file, HttpSession session) {
        iProductService.save(product, file, session);
        return REDIRECT_PRODUCTS;
    }

    @GetMapping(ProductControllerEndpointRoutes.EDIT)
    public String edit(@PathVariable String reference, Model model) {
        Product product = iProductService.findByReference(reference);
        model.addAttribute("product", product);

        return EDIT_VIEW;
    }

    @PostMapping(ProductControllerEndpointRoutes.UPDATE)
    public String update(Product product, @RequestParam("img") MultipartFile file, HttpSession session) {
        iProductService.update(product, file, session);
        return REDIRECT_PRODUCTS;
    }

    @GetMapping(ProductControllerEndpointRoutes.DELETE)
    public String delete(@PathVariable Integer id, HttpSession session) {
        iProductService.delete(id, session);
        return REDIRECT_PRODUCTS;
    }

}
