package com.ecommerce.controller;

import com.ecommerce.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.ecommerce.constants.messages.ExceptionHandlerErrorMessages.*;
import static com.ecommerce.constants.view.ViewConstants.ERROR_VIEW;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductException.class)
    public String handleProductServiceException(ProductException e, Model model) {
        logger.error(EXCEPTION_HANDLER_PRODUCT_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(OrderException.class)
    public String handleOrderServiceException(OrderException e, Model model) {
        logger.error(EXCEPTION_HANDLER_ORDER_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(OrderDetailsException.class)
    public String handleOrderDetailServiceException(OrderDetailsException e, Model model) {
        logger.error(EXCEPTION_HANDLER_ORDER_DETAILS_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(UserException.class)
    public String handleUserDetailException(UserException e, Model model) {
        logger.error(EXCEPTION_HANDLER_USER_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(DataAccessException.class)
    public String handleDataAccessException(DataAccessException e, Model model) {
        logger.error(EXCEPTION_HANDLER_DATA_ACCESS_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        logger.error(EXCEPTION_HANDLER_ILLEGAL_ARGUMENT_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }


    @ExceptionHandler(EmptyResultDataAccessException.class)
    public String handleEmptyResultDataAccessException(EmptyResultDataAccessException e, Model model) {
        logger.error(EXCEPTION_HANDLER_EMPTY_RESULT_DATA_ACCESS_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointerExceptionException(NullPointerException e, Model model) {
        logger.error(EXCEPTION_HANDLER_NULL_POINTER_ERROR, e);
        model.addAttribute("errorMessage", e);
        return ERROR_VIEW;
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.error(EXCEPTION_HANDLER_EXCEPTION_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e);
        return ERROR_VIEW;
    }


}
