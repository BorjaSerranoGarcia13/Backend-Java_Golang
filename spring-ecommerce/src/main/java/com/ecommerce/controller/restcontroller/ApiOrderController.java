package com.ecommerce.controller.restcontroller;

import com.ecommerce.constants.endpoints.api.ApiOrderEndpointRoutes;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.model.Order;
import com.ecommerce.service.IOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiOrderController {
    private final IOrderService iOrderService;

    public ApiOrderController(IOrderService iOrderService) {
        this.iOrderService = iOrderService;
    }

    @GetMapping(ApiOrderEndpointRoutes.API_ORDERS)
    public List<OrderDto> getAllOrders() {
        return iOrderService.convertOrderToDto(iOrderService.findAll());
    }

    @GetMapping(ApiOrderEndpointRoutes.API_ORDERS_PAGED)
    public Page<OrderDto> getAllOrdersPaged(Pageable pageable) {
        Page<Order> orderPage = iOrderService.findAll(pageable);
        return orderPage.map(iOrderService::convertOrderToDto);
    }

    @GetMapping(ApiOrderEndpointRoutes.API_ORDER_BY_ID)
    public OrderDto getOrderById(@PathVariable Integer id) {
        return iOrderService.convertOrderToDto(iOrderService.findById(id));
    }

    @GetMapping(ApiOrderEndpointRoutes.API_ORDER_BY_REFERENCE)
    public OrderDto getOrderByReference(@PathVariable String reference) {
        return iOrderService.convertOrderToDto(iOrderService.findByReference(reference));
    }

    @GetMapping(ApiOrderEndpointRoutes.API_ORDERS_BY_USER_ID)
    public List<OrderDto> getOrdersByUserId(@PathVariable Integer userId) {
        return iOrderService.convertOrderToDto(iOrderService.findByUserId(userId));
    }

    @DeleteMapping(ApiOrderEndpointRoutes.API_ORDERS_DELETE_BY_ID)
    public void deleteOrderById(@PathVariable Integer id) {
        iOrderService.delete(id);
    }
}