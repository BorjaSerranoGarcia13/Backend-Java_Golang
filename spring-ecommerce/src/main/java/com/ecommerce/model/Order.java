package com.ecommerce.model;

import com.ecommerce.constants.messages.OrderErrorMessages;
import com.ecommerce.exception.OrderException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.ecommerce.utils.ReferenceGeneratorUtils.ORDER_DETAIL_PREFIX;

@Entity
@Table(name = "orders", indexes = @Index(name = "index_reference", columnList = "reference", unique = true))
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String reference;
    private Date creationDate;
    private Date receivedDate;
    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetails> orderDetails;


    public Order() {

    }

    public Order(Integer id, String reference, Date creationDate, Date receivedDate, BigDecimal total, User user, List<OrderDetails> orderDetails) {
        this.id = id;
        this.reference = reference;
        this.creationDate = creationDate;
        this.receivedDate = receivedDate;
        this.total = total;
        this.user = user;
        this.orderDetails = orderDetails;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id <= 0) {
            throw new OrderException(OrderErrorMessages.ERROR_ORDER_INVALID_ID);
        }
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        if (reference == null) {
            throw new OrderException(OrderErrorMessages.ERROR_ORDER_INVALID_REFERENCE);
        }
        if (!reference.startsWith(ORDER_DETAIL_PREFIX)) {
            throw new OrderException(OrderErrorMessages.ERROR_ORDER_INVALID_REFERENCE_PREFIX);
        }
        this.reference = reference;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        if (creationDate == null) {
            throw new OrderException(OrderErrorMessages.ERROR_ORDER_INVALID_CREATION_DATE);
        }
        this.creationDate = creationDate;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        if (receivedDate == null) {
            throw new OrderException(OrderErrorMessages.ERROR_ORDER_INVALID_RECEIVED_DATE);
        }
        this.receivedDate = receivedDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderException(OrderErrorMessages.ERROR_ORDER_INVALID_TOTAL);
        }
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new OrderException(OrderErrorMessages.ERROR_ORDER_INVALID_USER);
        }
        this.user = user;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", creationDate=" + creationDate +
                ", receivedDate=" + receivedDate +
                ", total=" + total +
                ", user=" + user +
                '}';
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        if (orderDetails == null) {
            throw new OrderException(OrderErrorMessages.ERROR_ORDER_INVALID_ORDER_DETAILS);
        }
        this.orderDetails = orderDetails;
    }
}
