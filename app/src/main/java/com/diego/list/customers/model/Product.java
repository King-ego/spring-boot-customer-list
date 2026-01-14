package com.diego.list.customers.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(precision =  10, scale = 2)
    private BigDecimal price;

    private Integer quantity_in_stock;

    private String description;

    private String category;

    @Column(unique = true, nullable = false)
    private String slug;

    private String image_url;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}


/*
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(precision =  10, scale = 2)
    private BigDecimal price;

    private Integer quantity_in_stock;

    private String description;

    private String category;

    @Column(unique = true, nullable = false)
    private String slug;

    private String image_url;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Product() {
    }

   public Product(
           UUID id, String name, BigDecimal price, Integer quantity_in_stock,
           String description, String category, String slug, String image_url,
           LocalDateTime createdAt, LocalDateTime updatedAt
   ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity_in_stock = quantity_in_stock;
        this.description = description;
        this.category = category;
        this.slug = slug;
        this.image_url = image_url;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
   }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

   public void setId(UUID id) {
        this.id = id;
   }

    public UUID getId() {
          return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity_in_stock() {
        return quantity_in_stock;
    }

    public void setQuantity_in_stock(Integer quantity_in_stock) {
        this.quantity_in_stock = quantity_in_stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public  void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity_in_stock=" + quantity_in_stock +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", slug='" + slug + '\'' +
                ", image_url='" + image_url + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

    public static class ProductBuilder {
        private UUID id;
        private String name;
        private BigDecimal price;
        private Integer quantity_in_stock;
        private  String description;
        private String category;
        private String slug;
        private String image_url;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        ProductBuilder(){}

        public ProductBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductBuilder quantity_in_stock(Integer quantity_in_stock) {
            this.quantity_in_stock = quantity_in_stock;
            return this;
        }

        public  ProductBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder category(String category) {
            this.category = category;
            return this;
        }

        public ProductBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public ProductBuilder image_url(String image_url) {
            this.image_url = image_url;
            return this;
        }

        public  ProductBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ProductBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Product build() {
            return new Product(id, name, price, quantity_in_stock, description, category, slug, image_url, createdAt, updatedAt);
        }
    }
}*/
