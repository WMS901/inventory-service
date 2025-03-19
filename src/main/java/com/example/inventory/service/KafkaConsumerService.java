package com.example.inventory.service;

import com.example.inventory.dto.InventoryItemRequestDto;
import com.example.inventory.entity.InventoryItem;
import com.example.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final InventoryRepository inventoryRepository;

    @KafkaListener(topics = "inbound-confirmed", groupId = "inventory-group")
    public void consumeInboundConfirmed(InventoryItemRequestDto inboundItem) {
        log.info("Kafka 메시지 수신: {}", inboundItem);

        // 재고에 입고 정보 insert 해준당
        InventoryItem newItem = InventoryItem.builder()
                .sku(inboundItem.getName() + "-" + inboundItem.getCategory()) // SKU 생성 (예제)
                .name(inboundItem.getName())
                .category(inboundItem.getCategory())
                .quantity(inboundItem.getQuantity())
                .price(BigDecimal.valueOf(inboundItem.getPrice().doubleValue()))
                .supplier(inboundItem.getSupplier())
                .location(inboundItem.getLocation())
                .build();

        inventoryRepository.save(newItem);
        log.info("재고 데이터 추가 완료: {}", newItem);
    }
}
