package com.example.inventory.config;

import com.example.inventory.dto.InventoryItemRequestDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, InventoryItemRequestDto> inventoryConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("SPRING_KAFKA_BOOTSTRAP_SERVERS"));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, System.getenv("SPRING_KAFKA_CONSUMER_GROUP_ID"));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, System.getenv("SPRING_KAFKA_CONSUMER_AUTO_OFFSET_RESET"));
        props.put(JsonDeserializer.TRUSTED_PACKAGES, System.getenv("SPRING_KAFKA_CONSUMER_PROPERTIES_SPRING_JSON_TRUSTED_PACKAGES"));

        // ✅ typeId 없이 JSON 메시지를 받을 수 있도록 설정
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.inventory.dto.InventoryItemRequestDto");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(InventoryItemRequestDto.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InventoryItemRequestDto> inventoryKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, InventoryItemRequestDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(inventoryConsumerFactory());
        return factory;
    }
}
