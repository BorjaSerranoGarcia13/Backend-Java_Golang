package com.ecommerce.controller.viewcontroller;

import com.ecommerce.constants.endpoints.web.ProductControllerWebEndpointRoutes;
import com.ecommerce.controller.restcontroller.ApiProductController;
import com.ecommerce.dto.ProductDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static com.ecommerce.constants.redirect.RedirectConstants.REDIRECT_PRODUCTS;
import static com.ecommerce.constants.view.ViewConstants.ADMIN_CREATE_VIEW;
import static com.ecommerce.constants.view.ViewConstants.ADMIN_EDIT_VIEW;

@Controller
public class ProductController {
    private final ApiProductController apiProductController;

    public ProductController(ApiProductController apiProductController) {
        this.apiProductController = apiProductController;
    }

    @GetMapping(ProductControllerWebEndpointRoutes.CREATE)
    public String create() {
        return ADMIN_CREATE_VIEW;
    }

    @GetMapping(ProductControllerWebEndpointRoutes.EDIT)
    public String edit(@PathVariable Integer id, Model model) {
        ProductDto product = apiProductController.getProductById(id);
        model.addAttribute("product", product);

        return ADMIN_EDIT_VIEW;
    }

    @PostMapping(ProductControllerWebEndpointRoutes.SAVE)
    public String save(@ModelAttribute("product") ProductDto productDto) {
        apiProductController.saveProduct(productDto);

        return REDIRECT_PRODUCTS;
    }

    @PostMapping(ProductControllerWebEndpointRoutes.UPDATE)
    public String update(@ModelAttribute("product") ProductDto productDto) {
        apiProductController.updateProduct(productDto);
        return REDIRECT_PRODUCTS;
    }

    @PostMapping(ProductControllerWebEndpointRoutes.DELETE)
    public String delete(@PathVariable Integer id) {
        apiProductController.deleteProduct(id);

        return REDIRECT_PRODUCTS;
    }
}
