package com.ecommerce.controller.restcontroller;

import com.ecommerce.constants.endpoints.api.ApiProductEndpointRoutes;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.model.Product;
import com.ecommerce.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Product API", description = "API for managing products")
public class ApiProductController {
    private final IProductService iProductService;

    public ApiProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @GetMapping(ApiProductEndpointRoutes.API_PRODUCTS)
    @Operation(summary = "Get all products including deleted", description = "Fetch all products from the database including deleted ones")
    public List<ProductDto> getAllProductsIncludingDeleted() {
        return iProductService.convertProductToDto(iProductService.findAll());
    }

    @GetMapping(ApiProductEndpointRoutes.API_PRODUCTS_PAGED)
    @Operation(summary = "Get all products paged", description = "Fetch all products from the database with pagination")
    public Page<ProductDto> getAllProductsPaged(Pageable pageable) {
        Page<Product> productPage = iProductService.findAll(pageable);
        return productPage.map(iProductService::convertProductToDto);
    }

    @GetMapping(ApiProductEndpointRoutes.API_PRODUCT_BY_ID)
    @Operation(summary = "Get product by ID", description = "Fetch a product from the database by its ID")
    public ProductDto getProductById(@PathVariable Integer id) {
        return iProductService.convertProductToDto(iProductService.findById(id));
    }

    @GetMapping(ApiProductEndpointRoutes.API_PRODUCT_BY_REFERENCE)
    @Operation(summary = "Get product by exact reference", description = "Fetch a product from the database by its exact reference")
    public ProductDto getProductByExactReference(@PathVariable String reference) {
        return iProductService.convertProductToDto(iProductService.findByReference(reference));
    }

    @GetMapping(ApiProductEndpointRoutes.API_PRODUCTS_BY_REFERENCE)
    @Operation(summary = "Get products by partial reference", description = "Fetch products from the database by a partial reference")
    public List<ProductDto> getProductsByPartialReference(@PathVariable String reference) {
        return iProductService.convertProductToDto(iProductService.findProductsByReference(reference));
    }

    @PostMapping(ApiProductEndpointRoutes.API_PRODUCTS_SEARCH_BY_NAME_OR_REFERENCE)
    @Operation(summary = "Search products by name or reference", description = "Search products in the database by name or reference")
    public List<ProductDto> searchProductsByNameOrReference(@RequestParam String searchTerm,
                                                            @RequestParam String searchType) {
        return iProductService.convertProductToDto(iProductService.searchProductsByNameOrReference(searchTerm,
                searchType));
    }

    @PostMapping(ApiProductEndpointRoutes.API_PRODUCT_CREATE)
    @Operation(summary = "Create a product", description = "Create a new product in the database")
    public ProductDto saveProduct(@RequestBody ProductDto productDto) {
        return iProductService.convertProductToDto(iProductService.save(productDto));
    }

    @SuppressWarnings("UnusedReturnValue")
    @PutMapping(ApiProductEndpointRoutes.API_PRODUCT_UPDATE)
    @Operation(summary = "Update a product", description = "Update an existing product in the database")
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return iProductService.convertProductToDto(iProductService.update(productDto));
    }

    @DeleteMapping(ApiProductEndpointRoutes.API_PRODUCT_DELETE)
    @Operation(summary = "Delete a product", description = "Delete a product from the database")
    public void deleteProduct(@PathVariable Integer id) {
        iProductService.delete(id);
    }
}