package com.ecommerce.dto;

import com.ecommerce.constants.messages.OrderExceptionMessages;
import com.ecommerce.exception.OrderException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.ecommerce.utils.ReferenceGeneratorUtil.ORDER_PREFIX;

public class OrderDto {
    private Integer id;
    private String reference;
    private Date creationDate;
    private Date receivedDate;
    private BigDecimal total;
    private Integer userId;
    private List<OrderDetailsDto> orderDetails;

    public OrderDto() {
    }

    public OrderDto(Integer id, String reference, Date creationDate, Date receivedDate, BigDecimal total, Integer userId,
                    List<OrderDetailsDto> orderDetails) {
        this.id = id;
        this.reference = reference;
        this.creationDate = creationDate;
        this.receivedDate = receivedDate;
        this.total = total;
        this.userId = userId;
        this.orderDetails = orderDetails;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id <= 0) {
            throw new OrderException(OrderExceptionMessages.ERROR_ORDER_INVALID_ID);
        }
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        if (reference == null) {
            throw new OrderException(OrderExceptionMessages.ERROR_ORDER_INVALID_REFERENCE);
        }
        if (!reference.startsWith(ORDER_PREFIX)) {
            throw new OrderException(OrderExceptionMessages.ERROR_ORDER_INVALID_REFERENCE_PREFIX);
        }
        this.reference = reference;
    }

    public java.util.Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(java.util.Date creationDate) {
        if (creationDate == null) {
            throw new OrderException(OrderExceptionMessages.ERROR_ORDER_INVALID_CREATION_DATE);
        }
        this.creationDate = creationDate;
    }

    public java.util.Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(java.util.Date receivedDate) {
        if (receivedDate == null) {
            throw new OrderException(OrderExceptionMessages.ERROR_ORDER_INVALID_RECEIVED_DATE);
        }
        this.receivedDate = receivedDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderException(OrderExceptionMessages.ERROR_ORDER_INVALID_TOTAL);
        }
        this.total = total;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new OrderException(OrderExceptionMessages.ERROR_ORDER_INVALID_USER_ID);
        }
        this.userId = userId;
    }

    public List<OrderDetailsDto> getOrderDetails() {
        return orderDetails;
    }
    public void setOrderDetails(List<OrderDetailsDto> orderDetails) {
        if (orderDetails == null) {
            throw new OrderException(OrderExceptionMessages.ERROR_ORDER_INVALID_ORDER_DETAILS);
        }
        this.orderDetails = orderDetails;
    }
}
