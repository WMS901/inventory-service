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

    @KafkaListener(
        topics = "inbound-confirmed",
        groupId = "inventory-group",
        containerFactory = "inventoryKafkaListenerContainerFactory"
    )
    public void consumeInboundConfirmed(InventoryItemRequestDto inboundItem) {
        log.info("📥 Kafka 메시지 수신: {}", inboundItem);

        try {
            if (inboundItem.getSku() == null || inboundItem.getSku().isEmpty()) {
                log.warn("⚠️ SKU가 없는 메시지 수신. 처리 중단.");
                return;
            }

            // ✅ 기존 SKU 사용 (입고 서비스에서 생성한 SKU 유지)
            InventoryItem newItem = InventoryItem.builder()
                    .sku(inboundItem.getSku())
                    .name(inboundItem.getName())
                    .category(inboundItem.getCategory())
                    .quantity(inboundItem.getQuantity())
                    .price(BigDecimal.valueOf(inboundItem.getPrice().doubleValue()))
                    .supplier(inboundItem.getSupplier())
                    .location(inboundItem.getLocation())
                    .build();

            inventoryRepository.save(newItem);
            log.info("✅ 재고 데이터 추가 완료: {}", newItem);
        } catch (Exception e) {
            log.error("🚨 Kafka 메시지 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
