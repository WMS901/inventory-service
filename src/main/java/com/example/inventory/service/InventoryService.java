package com.example.inventory.service;

import com.example.inventory.dto.InventoryItemRequestDto;
import com.example.inventory.dto.InventoryItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InventoryService {
    InventoryItemResponseDto addItem(InventoryItemRequestDto itemDto);
    Page<InventoryItemResponseDto> getAllItems(Pageable pageable);
    Optional<InventoryItemResponseDto> getItemBySku(String sku);
    InventoryItemResponseDto updateItem(String sku, InventoryItemRequestDto itemDto);
    void deleteItem(String sku);
}
