package com.ecommerce.controller;

import static com.ecommerce.constants.view.ViewConstants.*;
import static com.ecommerce.constants.redirect.RedirectConstants.*;

import com.ecommerce.constants.endpoints.AdminEndpointRoutes;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.service.IOrderDetailService;
import com.ecommerce.service.IOrderService;
import com.ecommerce.service.IProductService;
import com.ecommerce.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final IProductService iProductService;
    private final IOrderService iOrderService;
    private final IOrderDetailService iOrderDetailService;
    private final IUserService iUserService;

    public AdminController(IProductService iProductService, IOrderService iOrderService, IOrderDetailService iOrderDetailService, IUserService iUserService) {
        this.iProductService = iProductService;
        this.iOrderService = iOrderService;
        this.iOrderDetailService = iOrderDetailService;
        this.iUserService = iUserService;
    }

    @GetMapping(AdminEndpointRoutes.HOME)
    public String homeView(Model model) {
        List<Product> products = iProductService.findAll();
        model.addAttribute("products", products);
        return ADMIN_HOME_VIEW;
    }

    @GetMapping(AdminEndpointRoutes.PRODUCT)
    public String productView(@PathVariable String reference, Model model) {
        Product product = iProductService.findByReference(reference);
        model.addAttribute("product", product);
        return PRODUCT_VIEW;
    }

    @GetMapping(AdminEndpointRoutes.ORDERS)
    public String ordersView(Model model) {
        List<Order> orders = iOrderService.findAll();
        model.addAttribute("orders", orders);
        return ADMIN_ORDERS_VIEW;
    }

    @GetMapping(AdminEndpointRoutes.ORDER_DETAILS)
    public String orderDetailView(@PathVariable String reference, Model model) {
        List<OrderDetails> orderDetails = iOrderDetailService.findByReference(reference);
        model.addAttribute("orderDetails", orderDetails);
        return ADMIN_ORDER_DETAIL_VIEW;
    }

    @GetMapping(AdminEndpointRoutes.USERS)
    public String usersView(Model model) {
        model.addAttribute("users", iUserService.findAll());
        return ADMIN_USERS_VIEW;
    }

    @PostMapping(AdminEndpointRoutes.USERS_DELETE)
    public String usersDelete(@PathVariable Integer id, Model model) {
        iUserService.delete(id);
        return REDIRECT_USERS_VIEW;
    }

}
