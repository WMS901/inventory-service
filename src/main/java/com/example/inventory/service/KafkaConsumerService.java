package com.example.inventory.service;

import com.example.inventory.dto.InventoryItemRequestDto;
import com.example.inventory.entity.InventoryItem;
import com.example.inventory.repository.InventoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final InventoryRepository inventoryRepository;
    private final ObjectMapper objectMapper; // ✅ JSON 변환을 위한 ObjectMapper

    @KafkaListener(topics = "inbound-confirmed", groupId = "inventory-group")
    public void consumeInboundConfirmed(ConsumerRecord<String, String> record) {
        log.info("📥 Kafka 메시지 수신: {}", record.value());

        try {
            if (record.value() == null || record.value().isEmpty()) {
                log.warn("⚠️ 수신된 Kafka 메시지가 비어있음. 처리 중단.");
                return;
            }

            // ✅ JSON 문자열을 DTO 객체로 변환
            InventoryItemRequestDto inboundItem = objectMapper.readValue(record.value(), InventoryItemRequestDto.class);

            if (inboundItem.getSku() == null || inboundItem.getSku().isEmpty()) {
                log.warn("⚠️ SKU가 없는 메시지 수신. 처리 중단.");
                return;
            }

            // ✅ 기존 SKU 사용 (입고 서비스에서 생성한 SKU 유지)
            InventoryItem newItem = InventoryItem.builder()
                    .sku(inboundItem.getSku()) // ✅ 기존 SKU 유지
                    .name(inboundItem.getName())
                    .category(inboundItem.getCategory())
                    .quantity(inboundItem.getQuantity())
                    .price(inboundItem.getPrice())  // ✅ BigDecimal 변환 불필요
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
