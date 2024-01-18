package com.ecommerce.dto;

import com.ecommerce.constants.messages.OrderDetailsExceptionMessages;
import com.ecommerce.exception.OrderDetailsException;

import java.math.BigDecimal;

public class OrderDetailsDto {
    private Integer id;
    private Integer quantity;
    private BigDecimal total;
    private ProductDto product;

    public OrderDetailsDto() {
    }

    public OrderDetailsDto(Integer id, Integer quantity, BigDecimal total, ProductDto product) {
        this.id = id;
        this.quantity = quantity;
        this.total = total;
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id <= 0) {
            throw new OrderDetailsException(OrderDetailsExceptionMessages.ERROR_ORDER_DETAILS_INVALID_ID);
        }
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new OrderDetailsException(OrderDetailsExceptionMessages.ERROR_ORDER_DETAILS_INVALID_QUANTITY);
        }
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderDetailsException(OrderDetailsExceptionMessages.ERROR_ORDER_DETAILS_INVALID_TOTAL);
        }
        this.total = total;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        if (product == null) {
            throw new OrderDetailsException(OrderDetailsExceptionMessages.ERROR_ORDER_DETAILS_INVALID_PRODUCT);
        }
        this.product = product;
    }
}
