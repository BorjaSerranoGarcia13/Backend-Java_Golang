package com.ecommerce.controller.restcontroller;

import com.ecommerce.exception.*;
import com.ecommerce.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.ecommerce.constants.messages.ExceptionHandlerMessages.*;

@RestControllerAdvice
public class ApiGlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApiGlobalExceptionHandler.class);

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceException(ProductException e) {
        logger.error(PRODUCT_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), System.currentTimeMillis()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderServiceException(OrderException e) {
        logger.error(ORDER_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), System.currentTimeMillis()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderDetailsException.class)
    public ResponseEntity<ErrorResponse> handleOrderDetailServiceException(OrderDetailsException e) {
        logger.error(ORDER_DETAILS_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), System.currentTimeMillis()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserDetailException(UserException e) {
        logger.error(USER_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(),
                System.currentTimeMillis()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        logger.error(AUTHENTICATION_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED.value(),
                System.currentTimeMillis()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException e) {
        logger.error(DATA_ACCESS_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                System.currentTimeMillis()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        logger.error(AUTHENTICATION_SERVICE_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED.value(),
                System.currentTimeMillis()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error(ILLEGAL_ARGUMENT_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        logger.error(EMPTY_RESULT_DATA_ACCESS_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(),
                System.currentTimeMillis()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerExceptionException(NullPointerException e) {
        logger.error(NULL_POINTER_ERROR, e);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                System.currentTimeMillis()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.error(EXCEPTION_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                System.currentTimeMillis()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error(VALIDATION_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error(MESSAGE_NOT_READABLE_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        logger.error(METHOD_NOT_SUPPORTED_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value(),
                System.currentTimeMillis()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e) {
        logger.error(MEDIA_TYPE_NOT_SUPPORTED_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                System.currentTimeMillis()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException
                                                                                               e) {
        logger.error(ARGUMENT_TYPE_MISMATCH_ERROR, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }
}
