package com.example.inventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemRequestDto {
    @JsonProperty("sku")
    private String sku;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("reservedQuantity")
    private Integer reservedQuantity;
    
    @JsonProperty("price")
    private BigDecimal price;
    
    @JsonProperty("supplier")
    private String supplier;
    
    @JsonProperty("location")
    private String location;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
}
