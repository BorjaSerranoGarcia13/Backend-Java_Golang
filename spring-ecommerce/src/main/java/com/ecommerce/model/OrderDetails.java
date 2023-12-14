package com.ecommerce.model;

import com.ecommerce.constants.messages.OrderDetailsErrorMessages;
import com.ecommerce.exception.OrderDetailsException;
import jakarta.persistence.*;

import java.math.BigDecimal;

import static com.ecommerce.constants.messages.OrderDetailsErrorMessages.ERROR_ORDER_DETAILS_INVALID_ORDER;
import static com.ecommerce.utils.ReferenceGeneratorUtils.ORDER_DETAIL_PREFIX;

@Entity
@Table(name = "orderDetails", indexes = @Index(name = "index_reference", columnList = "reference"))
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String reference;
    private Integer quantity;
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    private Product product;

    public OrderDetails() {

    }

    public OrderDetails(String reference, int quantity, BigDecimal price, BigDecimal total, Product product) {
        this.reference = reference;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.product = product;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        if (id == null || id <= 0) {
            throw new OrderDetailsException(OrderDetailsErrorMessages.ERROR_ORDER_DETAILS_INVALID_ID);
        }
        this.id = id;
    }

    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        if (reference == null || !reference.startsWith(ORDER_DETAIL_PREFIX)) {
            throw new OrderDetailsException(OrderDetailsErrorMessages.ERROR_ORDER_DETAILS_INVALID_REFERENCE);
        }
        this.reference = reference;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new OrderDetailsException(OrderDetailsErrorMessages.ERROR_ORDER_DETAILS_INVALID_QUANTITY);
        }
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderDetailsException(OrderDetailsErrorMessages.ERROR_ORDER_DETAILS_INVALID_PRICE);
        }
        this.price = price;
    }

    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderDetailsException(OrderDetailsErrorMessages.ERROR_ORDER_DETAILS_INVALID_TOTAL);
        }
        this.total = total;
    }

    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        if (order == null) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_ORDER);
        }

        this.order = order;
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        if (product == null) {
            throw new OrderDetailsException(OrderDetailsErrorMessages.ERROR_ORDER_DETAILS_INVALID_PRODUCT);
        }
        this.product = product;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", total=" + total +
                '}';
    }
}
