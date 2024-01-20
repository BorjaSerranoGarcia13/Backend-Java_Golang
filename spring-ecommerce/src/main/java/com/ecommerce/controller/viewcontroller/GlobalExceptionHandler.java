package com.ecommerce.controller.viewcontroller;

import com.ecommerce.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.ecommerce.constants.messages.ExceptionHandlerMessages.*;
import static com.ecommerce.constants.view.ViewConstants.ERROR_VIEW;

@ControllerAdvice("com.ecommerce.controller.viewcontroller")
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductException.class)
    public String handleProductServiceException(ProductException e, Model model) {
        logger.error(PRODUCT_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(OrderException.class)
    public String handleOrderServiceException(OrderException e, Model model) {
        logger.error(ORDER_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(OrderDetailsException.class)
    public String handleOrderDetailServiceException(OrderDetailsException e, Model model) {
        logger.error(ORDER_DETAILS_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(UserException.class)
    public String handleUserDetailException(UserException e, Model model) {
        logger.error(USER_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException e, Model model) {
        logger.error(AUTHENTICATION_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(DataAccessException.class)
    public String handleDataAccessException(DataAccessException e, Model model) {
        logger.error(DATA_ACCESS_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        logger.error(ILLEGAL_ARGUMENT_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }


    @ExceptionHandler(EmptyResultDataAccessException.class)
    public String handleEmptyResultDataAccessException(EmptyResultDataAccessException e, Model model) {
        logger.error(EMPTY_RESULT_DATA_ACCESS_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointerExceptionException(NullPointerException e, Model model) {
        logger.error(NULL_POINTER_ERROR, e);
        model.addAttribute("errorMessage", e);
        return ERROR_VIEW;
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.error(EXCEPTION_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e);
        return ERROR_VIEW;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e, Model model) {
        logger.error(VALIDATION_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException e, Model model) {
        logger.error(MESSAGE_NOT_READABLE_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, Model model) {
        logger.error(METHOD_NOT_SUPPORTED_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public String handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, Model model) {
        logger.error(MEDIA_TYPE_NOT_SUPPORTED_ERROR, e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return ERROR_VIEW;
    }

}
