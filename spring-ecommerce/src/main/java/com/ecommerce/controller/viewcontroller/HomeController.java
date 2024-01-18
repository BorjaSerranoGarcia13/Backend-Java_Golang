package com.ecommerce.controller.viewcontroller;

import com.ecommerce.constants.endpoints.web.HomeControllerWebEndpointRoutes;
import com.ecommerce.controller.restcontroller.ApiOrderDetailsController;
import com.ecommerce.controller.restcontroller.ApiProductController;
import com.ecommerce.controller.restcontroller.ApiUserController;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.ecommerce.constants.redirect.RedirectConstants.REDIRECT_USER_CART;
import static com.ecommerce.constants.redirect.RedirectConstants.REDIRECT_USER_HOME;
import static com.ecommerce.constants.view.ViewConstants.*;

@Controller
public class HomeController {
    private final ApiProductController apiProductController;
    private final ApiOrderDetailsController apiOrderDetailsController;
    private final ApiUserController apiUserController;


    public HomeController(ApiProductController apiProductController,
                          ApiOrderDetailsController apiOrderDetailsController,
                          ApiUserController apiUserController) {
        this.apiProductController = apiProductController;
        this.apiOrderDetailsController = apiOrderDetailsController;
        this.apiUserController = apiUserController;
    }

    @ModelAttribute("isUserLoggedIn")
    public boolean isUserLoggedIn() {
        //System.out.println("isUserLoggedIn");
        return false;
    }

    @GetMapping(HomeControllerWebEndpointRoutes.HOME)
    public String home(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<ProductDto> productPage = apiProductController.getAllProductsPaged(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return USER_HOME_VIEW;
    }

    @GetMapping(HomeControllerWebEndpointRoutes.PRODUCT_BY_ID)
    public String showProductById(@PathVariable Integer id, Model model) {
        Map.Entry<ProductDto, Integer> productAndQuantity = apiOrderDetailsController.getProductAndStockInCurrentCart(id);

        model.addAttribute("product", productAndQuantity.getKey());
        model.addAttribute("stock", productAndQuantity.getValue());

        return USER_PRODUCT_VIEW;
    }

    @PostMapping(HomeControllerWebEndpointRoutes.ADD_TO_CART)
    public String addProductToCart(@RequestParam("productId") Integer productId,
                                   @RequestParam("productQuantity") Integer productQuantity) {
        apiOrderDetailsController.addProductToCart(productId, productQuantity);

        return REDIRECT_USER_HOME;
    }

    @GetMapping(HomeControllerWebEndpointRoutes.CART)
    public String showCart(Model model) {
        Map.Entry<List<ProductDto>, BigDecimal> ProductAndTotalPrice =
                apiOrderDetailsController.getCartAndTotalPrice();

        model.addAttribute("products", ProductAndTotalPrice.getKey());
        model.addAttribute("totalPrice", ProductAndTotalPrice.getValue());

        return USER_CART_VIEW;
    }

    @PostMapping(HomeControllerWebEndpointRoutes.DELETE_CART_PRODUCT)
    public String deleteProductCart(@PathVariable Integer productId) {
        apiOrderDetailsController.removeProductFromCart(productId);

        return REDIRECT_USER_CART;
    }

    @PostMapping(HomeControllerWebEndpointRoutes.PURCHASE_CONFIRM)
    public String confirmPurchaseAndSaveOrderDetails() {
        apiOrderDetailsController.confirmPurchaseAndCreateOrder();
        return REDIRECT_USER_HOME;
    }

    @GetMapping(HomeControllerWebEndpointRoutes.ORDER_SUMMARY)
    public String orderSummary(Model model) {
        UserDto user = apiUserController.getUserById(apiUserController.getCurrentUserId());

        Map.Entry<List<ProductDto>, BigDecimal> productsAndTotalPrice =
                apiOrderDetailsController.getCartAndTotalPrice();

        model.addAttribute("user", user);
        model.addAttribute("products", productsAndTotalPrice.getKey());
        model.addAttribute("totalPrice", productsAndTotalPrice.getValue());

        return USER_ORDER_VIEW;
    }

    @GetMapping(HomeControllerWebEndpointRoutes.PRODUCT_SEARCH)
    public String searchProductsByNameOrReference(@RequestParam("searchTerm") String searchTerm,
                                                  @RequestParam("searchType") String searchType,
                                                  Model model) {

        List<ProductDto> products = apiProductController.searchProductsByNameOrReference(searchTerm, searchType);

        model.addAttribute("products", products);
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 20);

        return USER_HOME_VIEW;
    }
}

