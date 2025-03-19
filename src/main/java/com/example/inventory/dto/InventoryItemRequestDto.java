package com.example.inventory.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemRequestDto {
    private String sku;      // SKU 추가 입고에서(kafka) 받아와야함.
    private String name;      // 상품명
    private String category;  // 카테고리
    private int quantity;     // 현재 재고 수량
    private BigDecimal price; // 가격
    private String supplier;  // 공급업체
    private String location;  // 창고 위치
}
