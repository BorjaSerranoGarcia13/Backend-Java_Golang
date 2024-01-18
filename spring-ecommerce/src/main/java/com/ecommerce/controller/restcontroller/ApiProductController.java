package com.ecommerce.controller.restcontroller;

import com.ecommerce.constants.endpoints.api.ApiProductEndpointRoutes;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.model.Product;
import com.ecommerce.service.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApiProductController {
    private final IProductService iProductService;

    public ApiProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @GetMapping(ApiProductEndpointRoutes.API_PRODUCTS)
    public List<ProductDto> getAllProductsIncludingDeleted() {
        return iProductService.convertProductToDto(iProductService.findAll());
    }

    @GetMapping(ApiProductEndpointRoutes.API_PRODUCTS_PAGED)
    public Page<ProductDto> getAllProductsPaged(Pageable pageable) {
        Page<Product> productPage = iProductService.findAll(pageable);
        return productPage.map(iProductService::convertProductToDto);
    }

    @GetMapping(ApiProductEndpointRoutes.API_PRODUCT_BY_ID)
    public ProductDto getProductById(@PathVariable Integer id) {
        return iProductService.convertProductToDto(iProductService.findById(id));
    }

    @GetMapping(ApiProductEndpointRoutes.API_PRODUCT_BY_REFERENCE)
    public ProductDto getProductByExactReference(@PathVariable String reference) {
        return iProductService.convertProductToDto(iProductService.findByReference(reference));
    }

    @GetMapping(ApiProductEndpointRoutes.API_PRODUCTS_BY_REFERENCE)
    public List<ProductDto> getProductsByPartialReference(@PathVariable String reference) {
        return iProductService.convertProductToDto(iProductService.findProductsByReference(reference));
    }

    @GetMapping(ApiProductEndpointRoutes.API_PRODUCTS_SEARCH_BY_NAME_OR_REFERENCE)
    public List<ProductDto> searchProductsByNameOrReference(@RequestParam String searchTerm,
                                                            @RequestParam String searchType) {
        return iProductService.convertProductToDto(iProductService.searchProductsByNameOrReference(searchTerm,
                searchType));
    }

    @PostMapping(ApiProductEndpointRoutes.API_PRODUCT_CREATE)
    public ProductDto saveProduct(@RequestBody ProductDto productDto) {
        return iProductService.convertProductToDto(iProductService.save(productDto));
    }

    @SuppressWarnings("UnusedReturnValue")
    @PutMapping(ApiProductEndpointRoutes.API_PRODUCT_UPDATE)
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return iProductService.convertProductToDto(iProductService.update(productDto));
    }

    @DeleteMapping(ApiProductEndpointRoutes.API_PRODUCT_DELETE)
    public void deleteProduct(@PathVariable Integer id) {
        iProductService.delete(id);
    }

}