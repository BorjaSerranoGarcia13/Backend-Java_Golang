package com.ecommerce.controller.restcontroller;

import com.ecommerce.constants.endpoints.api.ApiOrderDetailsEndpointRoutes;
import com.ecommerce.dto.OrderDetailsDto;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.service.IOrderDetailsService;
import com.ecommerce.service.IOrderService;
import com.ecommerce.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Order Details API", description = "API for managing order details")
public class ApiOrderDetailsController {
    private final IOrderDetailsService iOrderDetailsService;
    private final IProductService iProductService;
    private final IOrderService iOrderService;

    public ApiOrderDetailsController(IOrderDetailsService iOrderDetailsService, IProductService iProductService,
                                     IOrderService iOrderService) {
        this.iOrderDetailsService = iOrderDetailsService;
        this.iProductService = iProductService;
        this.iOrderService = iOrderService;
    }

    @GetMapping(ApiOrderDetailsEndpointRoutes.API_ORDER_DETAILS)
    @Operation(summary = "Get all order details", description = "Fetch all order details from the database")
    public List<OrderDetailsDto> getAllOrderDetails() {
        return iOrderDetailsService.convertOrderDetailsToDto(iOrderDetailsService.findAll());
    }

    @GetMapping(ApiOrderDetailsEndpointRoutes.API_ORDER_DETAILS_PAGED)
    @Operation(summary = "Get all order details paged", description = "Fetch all order details from the database with pagination")
    public Page<OrderDetailsDto> getAllOrderDetailsPaged(Pageable pageable) {
        Page<OrderDetails> orderDetailsPage = iOrderDetailsService.findAll(pageable);
        return orderDetailsPage.map(iOrderDetailsService::convertOrderDetailsToDto);
    }

    @GetMapping(ApiOrderDetailsEndpointRoutes.API_ORDER_DETAIL_BY_ID)
    @Operation(summary = "Get order detail by ID", description = "Fetch an order detail from the database by its ID")
    public OrderDetailsDto getOrderDetailsById(@PathVariable Integer id) {
        return iOrderDetailsService.convertOrderDetailsToDto(iOrderDetailsService.findById(id));
    }

    @GetMapping(ApiOrderDetailsEndpointRoutes.API_ORDER_DETAILS_PRODUCT_STOCK)
    @Operation(summary = "Get product and stock in current cart", description = "Fetch a product and its stock in the " +
            "current cart")
    public Map.Entry<ProductDto, Integer> getProductAndStockInCurrentCart(@PathVariable Integer productId) {
        Map.Entry<Product, Integer> productAndStock = iOrderDetailsService.getCurrentStockById(productId);
        return new AbstractMap.SimpleEntry<>(iProductService.convertProductToDto(productAndStock.getKey()),
                productAndStock.getValue());
    }

    @GetMapping(ApiOrderDetailsEndpointRoutes.API_ORDER_DETAILS_CART)
    @Operation(summary = "Get cart and total price", description = "Fetch the current cart and its total price")
    public Map.Entry<List<ProductDto>, BigDecimal> getCartAndTotalPrice() {
        Map.Entry<List<Product>, BigDecimal> productsAndPrice = iOrderDetailsService.getCartAndTotalPrice();
        List<ProductDto> productDtoList = iProductService.convertProductToDto(
                productsAndPrice.getKey());
        return new AbstractMap.SimpleEntry<>(productDtoList, productsAndPrice.getValue());
    }

    @PostMapping(ApiOrderDetailsEndpointRoutes.API_ORDER_DETAILS_ADD_TO_CART)
    @Operation(summary = "Add product to cart", description = "Add a product to the cart")
    public ProductDto addProductToCart(@RequestParam Integer productId, @RequestParam Integer productQuantity) {
        return iProductService.convertProductToDto(iOrderDetailsService.addProductToCart(
                productId, productQuantity));
    }

    @DeleteMapping(ApiOrderDetailsEndpointRoutes.API_ORDER_DETAILS_REMOVE_FROM_CART)
    @Operation(summary = "Remove product from cart", description = "Remove a product from the cart")
    public void removeProductFromCart(@PathVariable Integer productId) {
        iOrderDetailsService.removeProductFromCart(productId);
    }

    @PostMapping(ApiOrderDetailsEndpointRoutes.API_ORDER_DETAILS_CONFIRM_PURCHASE)
    @Operation(summary = "Confirm purchase and create order", description = "Confirm the purchase and create an order")
    public OrderDto confirmPurchaseAndCreateOrder() {
        return iOrderService.convertOrderToDto(iOrderDetailsService.save());
    }
}
