package com.ecommerce.controller.restcontroller;

import com.ecommerce.constants.endpoints.api.ApiOrderEndpointRoutes;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.model.Order;
import com.ecommerce.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Order API", description = "API for managing orders")
public class ApiOrderController {
    private final IOrderService iOrderService;

    public ApiOrderController(IOrderService iOrderService) {
        this.iOrderService = iOrderService;
    }

    @GetMapping(ApiOrderEndpointRoutes.API_ORDERS)
    @Operation(summary = "Get all orders", description = "Fetch all orders from the database")
    public List<OrderDto> getAllOrders() {
        return iOrderService.convertOrderToDto(iOrderService.findAll());
    }

    @GetMapping(ApiOrderEndpointRoutes.API_ORDERS_PAGED)
    @Operation(summary = "Get all orders paged", description = "Fetch all orders from the database with pagination")
    public Page<OrderDto> getAllOrdersPaged(Pageable pageable) {
        Page<Order> orderPage = iOrderService.findAll(pageable);
        return orderPage.map(iOrderService::convertOrderToDto);
    }

    @GetMapping(ApiOrderEndpointRoutes.API_ORDER_BY_ID)
    @Operation(summary = "Get order by ID", description = "Fetch an order from the database by its ID")
    public OrderDto getOrderById(@PathVariable Integer id) {
        return iOrderService.convertOrderToDto(iOrderService.findById(id));
    }

    @GetMapping(ApiOrderEndpointRoutes.API_ORDER_BY_REFERENCE)
    @Operation(summary = "Get order by reference", description = "Fetch an order from the database by its reference")
    public OrderDto getOrderByReference(@PathVariable String reference) {
        return iOrderService.convertOrderToDto(iOrderService.findByReference(reference));
    }

    @GetMapping(ApiOrderEndpointRoutes.API_ORDERS_BY_USER_ID)
    @Operation(summary = "Get orders by user ID", description = "Fetch all orders from the database for a specific user")
    public List<OrderDto> getOrdersByUserId(@PathVariable Integer userId) {
        return iOrderService.convertOrderToDto(iOrderService.findByUserId(userId));
    }

    @DeleteMapping(ApiOrderEndpointRoutes.API_ORDERS_DELETE_BY_ID)
    @Operation(summary = "Delete order by ID", description = "Delete an order from the database by its ID")
    public void deleteOrderById(@PathVariable Integer id) {
        iOrderService.delete(id);
    }
}