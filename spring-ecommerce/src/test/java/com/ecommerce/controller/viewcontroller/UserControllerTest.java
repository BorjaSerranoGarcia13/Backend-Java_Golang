package com.ecommerce.controller.viewcontroller;

import com.ecommerce.controller.restcontroller.ApiOrderController;
import com.ecommerce.controller.viewcontroller.UserController;
import com.ecommerce.controller.restcontroller.ApiUserController;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.UserDto;
import com.ecommerce.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ecommerce.constants.redirect.RedirectConstants.*;
import static com.ecommerce.constants.view.ViewConstants.*;
import static com.ecommerce.constants.view.ViewConstants.USER_ORDER_DETAILS_VIEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private ApiUserController apiUserController;

    @Mock
    private ApiOrderController apiOrderController;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @Test
    void create() {
        String view = userController.create(model);

        assertEquals(USER_REGISTER_VIEW, view);
    }

    @Test
    void save() {
        User user = new User();
        when(apiUserController.isAdmin()).thenReturn(true);

        String view = userController.save(user);

        assertEquals(REDIRECT_ADMIN_HOME, view);
    }

    @Test
    void login() {
        String view = userController.login();

        assertEquals(USER_LOGIN_VIEW, view);
    }

    @Test
    void authenticateUserAsAdmin() {
        UserDto userDto = new UserDto();
        when(apiUserController.login(anyString(), anyString())).thenReturn(userDto);
        when(apiUserController.isAdmin()).thenReturn(true);

        String view = userController.authenticateUser("username", "password");

        assertEquals(REDIRECT_ADMIN_HOME, view);
    }

    @Test
    void authenticateUserAsRegularUser() {
        UserDto userDto = new UserDto();
        when(apiUserController.login(anyString(), anyString())).thenReturn(userDto);
        when(apiUserController.isAdmin()).thenReturn(false);

        String view = userController.authenticateUser("username", "password");

        assertEquals(REDIRECT_USER_HOME, view);
    }

    @Test
    void logoutUser() {
        String view = userController.logoutUser();

        assertEquals(REDIRECT_LOGIN_VIEW, view);
    }

    @Test
    void purchases() {
        List<OrderDto> ordersDto = new ArrayList<>();
        when(apiUserController.getCurrentUserId()).thenReturn(1);
        when(apiOrderController.getOrdersByUserId(anyInt())).thenReturn(ordersDto);

        String view = userController.purchases(model);

        assertEquals(USER_PURCHASES_VIEW, view);
    }

    @Test
    void purchaseDetails() {
        OrderDto orderDto = new OrderDto();
        when(apiOrderController.getOrderById(anyInt())).thenReturn(orderDto);

        String view = userController.purchaseDetails(1, model);

        assertEquals(USER_ORDER_DETAILS_VIEW, view);
        verify(model, times(1)).addAttribute("total", orderDto.getTotal());
        verify(model, times(1)).addAttribute("orderDetails", orderDto.getOrderDetails());
    }
}
