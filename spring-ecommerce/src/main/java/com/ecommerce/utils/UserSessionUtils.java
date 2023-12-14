package com.ecommerce.utils;

import com.ecommerce.model.User;
import com.ecommerce.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.ecommerce.constants.SessionAttributes.USER_ID;

@Component
public class UserSessionUtils {
    private final IUserService iUserService;

    public UserSessionUtils(IUserService iUserService) {
        this.iUserService = iUserService;
    }
    public Optional<User> checkVerifiedAdminFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(USER_ID);
        if (userId == null) {
            return Optional.empty();
        }

        User user = iUserService.findById(userId);

        if (!user.getAdmin()) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public Optional<User> checkVerifiedUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(USER_ID);
        if (userId == null) {
            return Optional.empty();
        }

        User user = iUserService.findById(userId);

        return Optional.of(user);
    }
}
