package com.ecommerce.controller;

import com.ecommerce.constants.endpoints.HomeControllerEndpointRoutes;

import static com.ecommerce.constants.view.ViewConstants.*;
import static com.ecommerce.constants.redirect.RedirectConstants.*;

import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.IOrderDetailService;
import com.ecommerce.service.IProductService;
import com.ecommerce.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.ecommerce.constants.SessionAttributes.USER_ID;

@Controller
@RequestMapping("/")
public class HomeController {
    private final IOrderDetailService iOrderDetailService;
    private final IProductService iProductService;
    private final IUserService iUserService;

    public HomeController(IProductService iProductService, IOrderDetailService iOrderDetailService, IUserService iUserService) {
        this.iProductService = iProductService;
        this.iOrderDetailService = iOrderDetailService;
        this.iUserService = iUserService;
    }

    @GetMapping(HomeControllerEndpointRoutes.HOME)
    public String home(Model model, HttpSession session) {
        List<Product> products = iProductService.findAll();
        model.addAttribute("products", products);

        return USER_HOME_VIEW;
    }

    @GetMapping(HomeControllerEndpointRoutes.PRODUCT)
    public String product(@PathVariable String reference, Model model) {
        Product product = iProductService.findByReference(reference);
        model.addAttribute("product", product);

        Integer stock = product.getQuantity() - iOrderDetailService.getCurrentOrderDetailsProductQuantity(reference);
        model.addAttribute("stock", stock);

        return USER_PRODUCT_VIEW;
    }

    @PostMapping(HomeControllerEndpointRoutes.ADD_TO_CART)
    public String addToCart(@RequestParam("reference") String reference, @RequestParam("quantity") Integer quantity) {
        iOrderDetailService.addProductToOrderDetailList(reference, quantity);
        return REDIRECT_USER_HOME;
    }

    @GetMapping(HomeControllerEndpointRoutes.CART)
    public String cart(Model model) {
        List<OrderDetails> orderDetails = iOrderDetailService.getOrderDetailsList();
        model.addAttribute("orderDetails", orderDetails);

        BigDecimal totalPrice = iOrderDetailService.getTotalPrice();
        model.addAttribute("totalPrice", totalPrice);

        return USER_CART_VIEW;
    }

    @GetMapping(HomeControllerEndpointRoutes.DELETE_CART_PRODUCT)
    public String deleteProductCart(@PathVariable Integer id) {
        iOrderDetailService.deleteProductInOrderDetailList(id);
        return REDIRECT_USER_CART;
    }

    @GetMapping(HomeControllerEndpointRoutes.PURCHASE_CONFIRM)
    public String saveOrderDetails(HttpSession session) {
        iOrderDetailService.save(session);
        return REDIRECT_USER_HOME;
    }

    @GetMapping(HomeControllerEndpointRoutes.ORDER)
    public String orderSummary(Model model, HttpSession session) {
        List<OrderDetails> orderDetails = iOrderDetailService.getOrderDetailsList();
        model.addAttribute("orderDetails", orderDetails);

        if (session.getAttribute(USER_ID) != null) {
            User user = iUserService.findById((Integer) session.getAttribute(USER_ID));
            model.addAttribute("user", user);
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderDetails orderDetail : orderDetails) {
            totalPrice = totalPrice.add(orderDetail.getProduct().getPrice().multiply(new BigDecimal(orderDetail.getQuantity())));
        }
        model.addAttribute("total", totalPrice);

        return USER_ORDER_VIEW;
    }

    @PostMapping(HomeControllerEndpointRoutes.SEARCH)
    public String searchProduct(@RequestParam("searchTerm") String searchTerm, @RequestParam("searchType") String searchType, Model model) {
        if (searchType.equals("name")) {
            List<Product> products = iProductService.findProductsByName(searchTerm);
            model.addAttribute("products", products);
        } else if (searchType.equals("reference")) {
            Product product = iProductService.findByReference(searchTerm);
            model.addAttribute("products", product);
        }
        return USER_HOME_VIEW;
    }


}

