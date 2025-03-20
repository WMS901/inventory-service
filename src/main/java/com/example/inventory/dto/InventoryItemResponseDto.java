package com.example.inventory.dto;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemResponseDto {
    private String sku;
    private String name;
    private String category;
    private int quantity;
    private int reservedQuantity;
    private BigDecimal price;
    private String supplier;
    private String location;
    private LocalDateTime createdAt;
}
