package com.ecommerce.controller;

import com.ecommerce.constants.endpoints.UserEndpointRoutes;

import static com.ecommerce.constants.view.ViewConstants.*;
import static com.ecommerce.constants.redirect.RedirectConstants.*;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import com.ecommerce.service.IOrderService;
import com.ecommerce.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.ecommerce.constants.SessionAttributes.USER_ID;

@Controller
@RequestMapping("/user")
public class UserController {
    private final IUserService iUserService;
    private final IOrderService iOrderService;

    public UserController(IOrderService iOrderService, IUserService iUserService) {
        this.iOrderService = iOrderService;
        this.iUserService = iUserService;
    }

    @GetMapping(UserEndpointRoutes.REGISTER)
    public String register(Model model) {
        model.addAttribute("user", new User());
        return USER_REGISTER_VIEW;
    }

    @PostMapping(UserEndpointRoutes.SAVE)
    public String save(@ModelAttribute("user") User user, HttpSession session) {
        User newUser = iUserService.save(user);
        session.setAttribute(USER_ID, newUser.getId());

        if (user.getAdmin()) {
            return REDIRECT_ADMIN_HOME;
        } else {
            return REDIRECT_USER_HOME;
        }
    }

    @GetMapping(UserEndpointRoutes.LOGIN)
    public String login() {
        return USER_LOGIN_VIEW;
    }

    @GetMapping(UserEndpointRoutes.DASHBOARD)
    public String dashboard(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        User user = iUserService.login(username, password);

        session.setAttribute(USER_ID, user.getId());

        if (user.getAdmin()) {
            return REDIRECT_ADMIN_HOME;
        } else {
            return REDIRECT_USER_HOME;
        }
    }

    @GetMapping(UserEndpointRoutes.LOGOUT)
    public String logoutUser(HttpSession session) {
        if (session != null && session.getAttribute(USER_ID) != null) {
            session.invalidate();
        }
        return REDIRECT_LOGIN_VIEW;
    }

    @GetMapping(UserEndpointRoutes.PURCHASES)
    public String purchases(Model model, HttpSession session) {
        User user = iUserService.findById((Integer) session.getAttribute(USER_ID));
        model.addAttribute("orders", user.getOrders());
        return USER_PURCHASES_VIEW;
    }

    @GetMapping(UserEndpointRoutes.PURCHASE_DETAILS)
    public String purchaseDetails(@PathVariable("reference") String reference, Model model) {
        Order order = iOrderService.findByReference(reference);
        model.addAttribute("orderDetails", order.getOrderDetails());
        return USER_ORDER_DETAILS_VIEW;
    }


}
