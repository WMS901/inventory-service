package com.example.inventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "inventory-updated";

    public void sendInventoryUpdate(String sku, int reservedQuantity, String name, String category, int quantity, BigDecimal price, String supplier, String location) {
        Map<String, Object> message = new HashMap<>();
        message.put("sku", sku);
        message.put("reservedQuantity", reservedQuantity);
        message.put("name", name);
        message.put("category", category);
        message.put("quantity", quantity);
        message.put("price", price);
        message.put("supplier", supplier);
        message.put("location", location);
        kafkaTemplate.send(TOPIC, message);
    }
}
