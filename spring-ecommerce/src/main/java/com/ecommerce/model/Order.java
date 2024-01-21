package com.ecommerce.model;

import com.ecommerce.constants.messages.OrderExceptionMessages;
import com.ecommerce.exception.OrderException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.ecommerce.utils.ReferenceGeneratorUtil.ORDER_PREFIX;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "orders", indexes = @Index(name = "index_reference", columnList = "reference", unique = true))
@Schema(description = "Order entity")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the order", example = "1")
    private Integer id;

    @Column(unique = true)
    @Schema(description = "Reference of the order", example = "ORD123")
    private String reference;

    @Schema(description = "Creation date of the order", example = "2022-01-01T00:00:00.000+00:00")
    private Date creationDate;

    @Schema(description = "Received date of the order", example = "2022-01-02T00:00:00.000+00:00")
    private Date receivedDate;

    @Column(precision = 10, scale = 2)
    @Schema(description = "Total amount of the order", example = "100.00")
    private BigDecimal total;

    @Schema(description = "User ID associated with the order", example = "1")
    private Integer userId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    @Schema(description = "Details of the order")
    private List<OrderDetails> orderDetails;

    public Order() {

    }

    public Order(Integer id, String reference, Date creationDate, Date receivedDate, BigDecimal total, Integer userId,
                 List<OrderDetails> orderDetails) {
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new OrderException(OrderExceptionMessages.ERROR_ORDER_INVALID_USER_ID);
        }
        this.userId = userId;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        if (creationDate == null) {
            throw new OrderException(OrderExceptionMessages.ERROR_ORDER_INVALID_CREATION_DATE);
        }
        this.creationDate = creationDate;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
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

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        if (orderDetails == null) {
            throw new OrderException(OrderExceptionMessages.ERROR_ORDER_INVALID_ORDER_DETAILS);
        }
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", creationDate=" + creationDate +
                ", receivedDate=" + receivedDate +
                ", total=" + total +
                ", userId=" + userId +
                '}';
    }
}
