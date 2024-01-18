package com.ecommerce.controller.viewcontroller;

import com.ecommerce.constants.endpoints.web.UserWebEndpointRoutes;
import com.ecommerce.controller.restcontroller.ApiOrderController;
import com.ecommerce.controller.restcontroller.ApiUserController;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ecommerce.constants.redirect.RedirectConstants.*;
import static com.ecommerce.constants.view.ViewConstants.*;

@Controller
public class UserController {
    private final ApiUserController apiUserController;
    private final ApiOrderController apiOrderController;


    public UserController(ApiUserController apiUserController, ApiOrderController apiOrderController) {
        this.apiUserController = apiUserController;
        this.apiOrderController = apiOrderController;
    }

    @GetMapping(UserWebEndpointRoutes.CREATE)
    public String create(Model model) {
        model.addAttribute("user", new User());
        return USER_REGISTER_VIEW;
    }

    @PostMapping(UserWebEndpointRoutes.SAVE)
    public String save(@ModelAttribute("user") User user) {
        apiUserController.saveUser(user);

        if (apiUserController.isAdmin()) {
            return REDIRECT_ADMIN_HOME;
        } else {
            return REDIRECT_USER_HOME;
        }
    }

    @GetMapping(UserWebEndpointRoutes.LOGIN)
    public String login() {
        return USER_LOGIN_VIEW;
    }

    @PostMapping(UserWebEndpointRoutes.AUTHENTICATE)
    public String authenticateUser(@RequestParam("username") String username,
                                   @RequestParam("password") String password, HttpServletRequest request) {
        apiUserController.login(username, password);

        if (apiUserController.isAdmin()) {
            System.out.println("User is admin");
            return REDIRECT_ADMIN_HOME;
        } else {
            System.out.println("User not admin");
            return REDIRECT_USER_HOME;
        }
    }

    @GetMapping(UserWebEndpointRoutes.LOGOUT)
    public String logoutUser() {
        apiUserController.logout();

        return REDIRECT_LOGIN_VIEW;
    }

    @GetMapping(UserWebEndpointRoutes.PURCHASES)
    public String purchases(Model model) {
        Integer userId = apiUserController.getCurrentUserId();
        List<OrderDto> orders = apiOrderController.getOrdersByUserId(userId);

        model.addAttribute("orders", orders);
        return USER_PURCHASES_VIEW;
    }

    @GetMapping(UserWebEndpointRoutes.PURCHASE_DETAILS)
    public String purchaseDetails(@PathVariable("id") Integer id, Model model) {
        OrderDto order = apiOrderController.getOrderById(id);
        model.addAttribute("total", order.getTotal());
        model.addAttribute("orderDetails", order.getOrderDetails());

        return USER_ORDER_DETAILS_VIEW;
    }
}
