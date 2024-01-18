package com.ecommerce.model;

import com.ecommerce.constants.messages.OrderDetailsExceptionMessages;
import com.ecommerce.exception.OrderDetailsException;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "orderDetails")
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quantity;
    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderDetails() {

    }

    public OrderDetails(int quantity, BigDecimal total,
                        Product product) {
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new OrderDetailsException(OrderDetailsExceptionMessages.ERROR_ORDER_DETAILS_INVALID_PRODUCT);
        }
        this.product = product;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", total=" + total +
                ", product=" + product +
                '}';
    }
}
