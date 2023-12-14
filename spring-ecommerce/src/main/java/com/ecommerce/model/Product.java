package com.ecommerce.model;


import com.ecommerce.constants.messages.ProductErrorMessages;
import com.ecommerce.exception.ProductException;
import jakarta.persistence.*;

import java.math.BigDecimal;

import static com.ecommerce.utils.ReferenceGeneratorUtils.PRODUCT_PREFIX;

@Entity
@Table(name = "products", indexes = @Index(name = "index_reference", columnList = "reference", unique = true))
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String reference;
    private String name;
    private String description;
    private String image;
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    private Integer quantity;

    @ManyToOne
    private User user;

    public Product() {
    }

    public Product(Integer id, String reference, String name, String description, String image, BigDecimal price, int quantity, User user) {
        this.id = id;
        this.reference = reference;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        if (id == null || id <= 0) {
            throw new ProductException(ProductErrorMessages.ERROR_PRODUCT_INVALID_ID);
        }
        this.id = id;
    }

    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        if (reference == null || !reference.startsWith(PRODUCT_PREFIX)) {
            throw new ProductException(ProductErrorMessages.ERROR_PRODUCT_INVALID_REFERENCE);
        }
        this.reference = reference;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (name == null) {
            throw new ProductException(ProductErrorMessages.ERROR_PRODUCT_INVALID_NAME);
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        if (description == null) {
            throw new ProductException(ProductErrorMessages.ERROR_PRODUCT_INVALID_DESCRIPTION);
        }
        this.description = description;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        if (image == null || image.isEmpty()) {
            throw new ProductException(ProductErrorMessages.ERROR_PRODUCT_INVALID_IMAGE);
        }

        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductException(ProductErrorMessages.ERROR_PRODUCT_INVALID_PRICE);
        }
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new ProductException(ProductErrorMessages.ERROR_PRODUCT_INVALID_QUANTITY);
        }
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        if (user == null) {
            throw new ProductException(ProductErrorMessages.ERROR_PRODUCT_INVALID_USER);
        }
        this.user = user;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", reference=" + reference +
                '}';
    }

}

