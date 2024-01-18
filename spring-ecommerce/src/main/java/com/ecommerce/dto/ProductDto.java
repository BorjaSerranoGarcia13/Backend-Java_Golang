package com.ecommerce.dto;

import com.ecommerce.constants.messages.ProductExceptionMessages;
import com.ecommerce.exception.ProductException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.ecommerce.utils.ReferenceGeneratorUtil.PRODUCT_PREFIX;

public class ProductDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String reference;
    private boolean deleted;

    public ProductDto() {
    }

    public ProductDto(Integer id, String name, String description, BigDecimal price, Integer quantity, String reference,
                      boolean deleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.reference = reference;
        this.deleted = deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id < 0) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_ID);
        }
        this.id = id;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        if (reference == null || !reference.startsWith(PRODUCT_PREFIX)) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_REFERENCE);
        }
        this.reference = reference;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id= " + id +
                ", name= '" + name + '\'' +
                ", description= '" + description + '\'' +
                ", quantity= " + quantity +
                ", price= " + price +
                "}";
    }
}
