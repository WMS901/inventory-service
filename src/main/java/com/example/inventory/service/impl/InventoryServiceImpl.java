package com.example.inventory.service.impl;

import com.example.inventory.dto.InventoryItemRequestDto;
import com.example.inventory.dto.InventoryItemResponseDto;
import com.example.inventory.entity.InventoryItem;
import com.example.inventory.repository.InventoryRepository;
import com.example.inventory.service.InventoryService;
import com.example.inventory.service.impl.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final KafkaProducerService kafkaProducerService;

    private InventoryItemResponseDto convertToResponseDto(InventoryItem item) {
        return InventoryItemResponseDto.builder()
                .sku(item.getSku())
                .name(item.getName())
                .category(item.getCategory())
                .quantity(item.getQuantity())
                .reservedQuantity(item.getReservedQuantity())
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
                .reservedQuantity(itemDto.getReservedQuantity())
                .price(itemDto.getPrice())
                .supplier(itemDto.getSupplier())
                .location(itemDto.getLocation())
                .createdAt(itemDto.getCreatedAt())
                .build();
        return convertToResponseDto(inventoryRepository.save(item));
    }

    public Page<InventoryItemResponseDto> getAllItems(Pageable pageable) {
        return inventoryRepository.findAllByOrderByCreatedAtDesc(pageable).map(this::convertToResponseDto);
    }

    public Optional<InventoryItemResponseDto> getItemBySku(String sku) {
        return inventoryRepository.findBySku(sku).map(this::convertToResponseDto);
    }

    public InventoryItemResponseDto updateItem(String sku, InventoryItemRequestDto itemDto) {
        return inventoryRepository.findBySku(sku)
                .map(item -> {
                    // ✅ 이름, 카테고리 등 다른 필드 업데이트
                    Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
                    Optional.ofNullable(itemDto.getCategory()).ifPresent(item::setCategory);
                    Optional.ofNullable(itemDto.getQuantity()).ifPresent(item::setQuantity);
                    Optional.ofNullable(itemDto.getPrice()).ifPresent(item::setPrice);
                    Optional.ofNullable(itemDto.getSupplier()).ifPresent(item::setSupplier);
                    Optional.ofNullable(itemDto.getLocation()).ifPresent(item::setLocation);
                    item.setReservedQuantity(item.getReservedQuantity() + itemDto.getReservedQuantity());


                    // ✅ DB 저장
                    inventoryRepository.save(item);

                    // ✅ Kafka 메시지 발행 (출고 서비스로 데이터 전달)
                    kafkaProducerService.sendInventoryUpdate(
                            item.getSku(), item.getReservedQuantity(),
                            item.getName(), item.getCategory(),
                            item.getQuantity(), item.getPrice(),
                            item.getSupplier(), item.getLocation()
                    );

                    return convertToResponseDto(item);
                })
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public void deleteItem(String sku) {
        inventoryRepository.findBySku(sku).ifPresent(inventoryRepository::delete);
    }
}
