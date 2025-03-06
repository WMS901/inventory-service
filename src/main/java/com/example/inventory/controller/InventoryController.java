package com.example.inventory.controller;

import com.example.inventory.dto.InventoryItemRequestDto;
import com.example.inventory.dto.InventoryItemResponseDto;
import com.example.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryItemResponseDto> addItem(@RequestBody InventoryItemRequestDto itemDto) {
        return ResponseEntity.ok(inventoryService.addItem(itemDto));
    }

    @GetMapping
    public ResponseEntity<Page<InventoryItemResponseDto>> getAllItems(Pageable pageable) {
        return ResponseEntity.ok(inventoryService.getAllItems(pageable));
    }

    @GetMapping("/{sku}")
    public ResponseEntity<InventoryItemResponseDto> getItemBySku(@PathVariable String sku) {
        Optional<InventoryItemResponseDto> item = inventoryService.getItemBySku(sku);
        return item.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{sku}")
    public ResponseEntity<InventoryItemResponseDto> updateItem(
            @PathVariable String sku,
            @RequestBody InventoryItemRequestDto itemDto) {
        return ResponseEntity.ok(inventoryService.updateItem(sku, itemDto));
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> deleteItem(@PathVariable String sku) {
        inventoryService.deleteItem(sku);
        return ResponseEntity.noContent().build();
    }
}
