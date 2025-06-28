package com.donora.kafka.producer;

import com.donora.config.KafkaTopicConfig;
import com.donora.dto.kafka.ItemDonationKafkaMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ItemDonationKafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendItemDonationCreatedEvent(ItemDonationKafkaMessage message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(KafkaTopicConfig.ITEM_DONATION_TOPIC, jsonMessage);
            System.out.println("✅ Kafka Event Sent → " + jsonMessage);
        } catch (JsonProcessingException e) {
            System.err.println("❌ Failed to send Kafka message: " + e.getMessage());
        }
    }
}
