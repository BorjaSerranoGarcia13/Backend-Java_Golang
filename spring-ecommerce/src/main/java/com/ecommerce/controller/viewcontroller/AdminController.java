package com.ecommerce.controller.viewcontroller;

import com.ecommerce.constants.endpoints.web.AdminWebEndpointRoutes;
import com.ecommerce.controller.restcontroller.ApiOrderController;
import com.ecommerce.controller.restcontroller.ApiProductController;
import com.ecommerce.controller.restcontroller.ApiUserController;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

import static com.ecommerce.constants.redirect.RedirectConstants.REDIRECT_USERS_VIEW;
import static com.ecommerce.constants.view.ViewConstants.*;

@Controller
public class AdminController {
    private final ApiProductController apiProductController;
    private final ApiOrderController apiOrderController;
    private final ApiUserController apiUserController;

    public AdminController(ApiProductController apiProductController, ApiOrderController apiOrderController,
                           ApiUserController apiUserController) {
        this.apiProductController = apiProductController;
        this.apiOrderController = apiOrderController;
        this.apiUserController = apiUserController;
    }

    @GetMapping(AdminWebEndpointRoutes.HOME)
    public String homeView(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<ProductDto> productPage = apiProductController.getAllProductsPaged(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return ADMIN_HOME_VIEW;
    }

    @GetMapping(AdminWebEndpointRoutes.PRODUCTS)
    public String productsView(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<ProductDto> productPage = apiProductController.getAllProductsPaged(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return ADMIN_PRODUCTS_VIEW;
    }

    @GetMapping(AdminWebEndpointRoutes.PRODUCT_BY_ID)
    public String productHomeView(@PathVariable Integer id, Model model) {
        ProductDto product = apiProductController.getProductById(id);
        model.addAttribute("product", product);
        return ADMIN_PRODUCTHOME_VIEW;
    }

    @GetMapping(AdminWebEndpointRoutes.ORDERS)
    public String ordersView(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<OrderDto> orderPage = apiOrderController.getAllOrdersPaged(pageable);

        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        return ADMIN_ORDERS_VIEW;
    }

    @GetMapping(AdminWebEndpointRoutes.ORDER_DETAILS_BY_ID)
    public String orderDetailsView(@PathVariable Integer id, Model model) {
        OrderDto order = apiOrderController.getOrderById(id);
        model.addAttribute("totalOrder", order.getTotal());
        model.addAttribute("order", order);
        return ADMIN_ORDER_DETAIL_VIEW;
    }

    @GetMapping(AdminWebEndpointRoutes.USERS)
    public String usersView(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<UserDto> userPage = apiUserController.getAllUsersPaged(pageable);

        List<UserDto> users = userPage.getContent();

        List<Boolean> isAdminList = users.stream()
                .map(user -> user.getAuthorities().stream()
                        .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority())))
                .collect(Collectors.toList());

        model.addAttribute("isAdminList", isAdminList);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        return ADMIN_USERS_VIEW;
    }

    @PostMapping(AdminWebEndpointRoutes.USERS_DELETE_BY_ID)
    public String usersDelete(@PathVariable Integer id, Model model) {
        apiUserController.deleteUserById(id);
        return REDIRECT_USERS_VIEW;
    }
}
