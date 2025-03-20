package com.example.inventory.service;

import com.example.inventory.dto.InventoryItemRequestDto;
import com.example.inventory.dto.InventoryItemResponseDto;
import com.example.inventory.entity.InventoryItem;
import com.example.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    private InventoryItemResponseDto convertToResponseDto(InventoryItem item) {
        return InventoryItemResponseDto.builder()
                .sku(item.getSku())
                .name(item.getName())
                .category(item.getCategory())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .supplier(item.getSupplier())
                .location(item.getLocation())
                .createdAt(item.getCreatedAt())
                .build();
    }

    public InventoryItemResponseDto addItem(InventoryItemRequestDto itemDto) {
        InventoryItem item = InventoryItem.builder()
                .name(itemDto.getName())
                .category(itemDto.getCategory())
                .quantity(itemDto.getQuantity())
                .price(itemDto.getPrice())
                .supplier(itemDto.getSupplier())
                .location(itemDto.getLocation())
                .createdAt(itemDto.getCreatedAt())
                .build();
        return convertToResponseDto(inventoryRepository.save(item));
    }

    public Page<InventoryItemResponseDto> getAllItems(Pageable pageable) {
        return inventoryRepository.findAll(pageable).map(this::convertToResponseDto);
    }

    public Optional<InventoryItemResponseDto> getItemBySku(String sku) {
        return inventoryRepository.findBySku(sku).map(this::convertToResponseDto);
    }

    public InventoryItemResponseDto updateItem(String sku, InventoryItemRequestDto itemDto) {
        return inventoryRepository.findBySku(sku)
                .map(item -> {
                    item.setName(itemDto.getName());
                    item.setCategory(itemDto.getCategory());
                    item.setQuantity(itemDto.getQuantity());
                    item.setPrice(itemDto.getPrice());
                    item.setSupplier(itemDto.getSupplier());
                    item.setLocation(itemDto.getLocation());
                    return convertToResponseDto(inventoryRepository.save(item));
                })
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public void deleteItem(String sku) {
        inventoryRepository.findBySku(sku).ifPresent(inventoryRepository::delete);
    }
}
