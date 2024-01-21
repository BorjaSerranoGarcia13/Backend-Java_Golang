package com.ecommerce.model;


import com.ecommerce.constants.messages.ProductExceptionMessages;
import com.ecommerce.exception.ProductException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.ecommerce.utils.ReferenceGeneratorUtil.PRODUCT_PREFIX;

@Entity
@Table(name = "products", indexes = @Index(name = "index_reference", columnList = "reference", unique = true))
@Schema(description = "Product entity")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the product", example = "1")
    private Integer id;

    @Column(unique = true)
    @Schema(description = "Reference of the product", example = "PROD123")
    private String reference;

    @Schema(description = "User ID associated with the product", example = "1")
    private Integer userId;

    @Schema(description = "Name of the product", example = "Product Name")
    private String name;

    @Schema(description = "Description of the product", example = "This is a product description")
    private String description;

    @Column(precision = 10, scale = 2)
    @Schema(description = "Price of the product", example = "100.00")
    private BigDecimal price;

    @Schema(description = "Quantity of the product in stock", example = "10")
    private Integer quantity;

    @Column(name = "deleted")
    @Schema(description = "Is the product deleted", example = "false")
    private boolean isDeleted = false;

    public Product() {
    }

    public Product(Integer id, String reference, String name, String description, BigDecimal price, int quantity, Integer userId) {
        this.id = id;
        this.reference = reference;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id <= 0) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_ID);
        }
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        if (reference == null || !reference.startsWith(PRODUCT_PREFIX)) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_REFERENCE);
        }
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_NAME);
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_DESCRIPTION);
        }
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_PRICE);
        }
        this.price = price.setScale(2, RoundingMode.DOWN);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_QUANTITY);
        }
        this.quantity = quantity;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_USER_ID);
        }
        this.userId = userId;
    }

    public boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", reference=" + reference +
                '}';
    }

}

