package com.ecommerce.controller;

import com.ecommerce.constants.endpoints.web.AdminWebEndpointRoutes;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.IOrderDetailsService;
import com.ecommerce.service.IOrderService;
import com.ecommerce.service.IProductService;
import com.ecommerce.service.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.math.BigDecimal;

import static com.ecommerce.constants.redirect.RedirectConstants.REDIRECT_USERS_VIEW;
import static com.ecommerce.constants.view.ViewConstants.*;

@Controller
@RequestMapping(AdminWebEndpointRoutes.ADMIN)
public class AdminController {
    private final IProductService iProductService;
    private final IOrderService iOrderService;
    private final IOrderDetailsService iOrderDetailsService;
    private final IUserService iUserService;

    public AdminController(IProductService iProductService, IOrderService iOrderService, IOrderDetailsService iOrderDetailsService, IUserService iUserService) {
        this.iProductService = iProductService;
        this.iOrderService = iOrderService;
        this.iOrderDetailsService = iOrderDetailsService;
        this.iUserService = iUserService;
    }

    @GetMapping(AdminWebEndpointRoutes.HOME)
    public String homeView(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<Product> orderPage = iProductService.findAll(pageable);

        model.addAttribute("products", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());

        return ADMIN_HOME_VIEW;
    }

    @GetMapping(AdminWebEndpointRoutes.PRODUCT)
    public String productView(@PathVariable String reference, Model model) {
        Product product = iProductService.findByReference(reference);
        model.addAttribute("product", product);
        return PRODUCT_VIEW;
    }

    @GetMapping(AdminWebEndpointRoutes.ORDERS)
    public String ordersView(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Order> orderPage = iOrderService.findAll(pageable);

        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        return ADMIN_ORDERS_VIEW;
    }

    @GetMapping(AdminWebEndpointRoutes.ORDER_DETAILS)
    public String orderDetailView(@PathVariable String reference, Model model) {
        List<OrderDetails> orderDetails = iOrderDetailsService.findByReference(reference);
        model.addAttribute("orderDetails", orderDetails);

        BigDecimal totalCost = orderDetails.stream()
                .map(OrderDetails::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("total", totalCost);

        return ADMIN_ORDER_DETAIL_VIEW;
    }

    @GetMapping(AdminWebEndpointRoutes.USERS)
    public String usersView(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<User> userPage = iUserService.findAll(pageable);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        return ADMIN_USERS_VIEW;
    }

    @PostMapping(AdminWebEndpointRoutes.USERS_DELETE)
    public String usersDelete(@PathVariable Integer id, Model model) {
        iUserService.delete(id);
        return REDIRECT_USERS_VIEW;
    }

}
