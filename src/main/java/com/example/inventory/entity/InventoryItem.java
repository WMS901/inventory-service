package com.example.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int reservedQuantity = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 100)
    private String supplier;

    @Column(length = 50)
    private String location;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.sku == null || this.sku.isEmpty()) {
            this.sku = generateSku(this.category, this.name);
        }

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    private String generateSku(String category, String productName) {
        String categoryCode = category.substring(0, 3).toUpperCase();
        String productCode = productName.replaceAll("\\s+", "").substring(0, 3).toUpperCase();
        String dateCode = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyMMdd"));
        int randomNum = new Random().nextInt(900) + 100;
        return categoryCode + "-" + productCode + "-" + dateCode + "-" + randomNum;
    }
}
