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
    @JsonProperty("sku")        // ✅ JSON 필드 매칭 문제 방지
    private String sku;         // SKU (입고에서 받은 값)
    
    @JsonProperty("name")
    private String name;        // 상품명
    
    @JsonProperty("category")
    private String category;    // 카테고리
    
    @JsonProperty("quantity")
    private int quantity;       // 현재 재고 수량
    
    @JsonProperty("price")
    private BigDecimal price;   // 가격
    
    @JsonProperty("supplier")
    private String supplier;    // 공급업체
    
    @JsonProperty("location")
    private String location;    // 창고 위치

    @JsonProperty("createdAt")  // ✅ createdAt 필드 추가
    private LocalDateTime createdAt;  // 생성일
}
