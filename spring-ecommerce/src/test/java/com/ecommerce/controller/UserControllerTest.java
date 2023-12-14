package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import com.ecommerce.service.IOrderService;
import com.ecommerce.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static com.ecommerce.constants.SessionAttributes.USER_ID;
import static com.ecommerce.constants.redirect.RedirectConstants.*;
import static com.ecommerce.constants.view.ViewConstants.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private IUserService iUserService;

    @Mock
    private IOrderService iOrderService;

    @Mock
    private HttpSession session;

    private Model model;
    private UserController userController;

    @BeforeEach
    public void setup() {
        assertDoesNotThrow(() -> MockitoAnnotations.openMocks(this));
        userController = new UserController(iOrderService, iUserService);
        model = new BindingAwareModelMap();
    }

    @Test
    public void testRegister() {
        String view = userController.register(model);
        assertEquals(USER_REGISTER_VIEW, view);

        User user = (User) model.getAttribute("user");
        assertEquals(user, model.getAttribute("user"));
    }

    @Test
    public void testSave() {
        User user = new User();
        user.setAdmin(false);

        User newUser = new User();
        newUser.setId(1);
        newUser.setAdmin(user.getAdmin());

        when(iUserService.save(user)).thenReturn(newUser);

        String view = userController.save(user, session);

        assertEquals(REDIRECT_USER_HOME, view);
        verify(session, times(1)).setAttribute(USER_ID, newUser.getId());
    }

    @Test
    public void testLogin() {
        String view = userController.login();
        assertEquals(USER_LOGIN_VIEW, view);
    }

    @Test
    public void testDashboard() {
        String username = "test";
        String password = "test";
        User user = new User();
        user.setAdmin(false);

        when(iUserService.login(username, password)).thenReturn(user);

        String view = userController.dashboard(username, password, session);

        if (user.getAdmin() == null) {
            assertEquals(REDIRECT_ADMIN_HOME, view);
        } else {
            assertEquals(REDIRECT_USER_HOME, view);
        }
        verify(session, times(1)).setAttribute(USER_ID, user.getId());
    }

    @Test
    public void testLogoutUser() {
        when(session.getAttribute(USER_ID)).thenReturn(1);

        String view = userController.logoutUser(session);

        assertEquals(REDIRECT_LOGIN_VIEW, view);
        verify(session, times(1)).invalidate();
    }

    @Test
    public void testPurchases() {
        User user = new User();
        List<Order> orders = Arrays.asList(new Order(), new Order());
        user.setOrders(orders);

        when(session.getAttribute(USER_ID)).thenReturn(1);
        when(iUserService.findById(1)).thenReturn(user);

        String view = userController.purchases(model, session);

        assertEquals(USER_PURCHASES_VIEW, view);
        assertEquals(orders, model.getAttribute("orders"));
    }

    @Test
    public void testPurchaseDetails() {
        String reference = "OrderRef";
        Order order = new Order();

        when(iOrderService.findByReference(reference)).thenReturn(order);

        String view = userController.purchaseDetails(reference, model);

        assertEquals(USER_ORDER_DETAILS_VIEW, view);
        assertEquals(order.getOrderDetails(), model.getAttribute("orderDetails"));
    }
}
