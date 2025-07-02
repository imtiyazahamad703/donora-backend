package com.donora.kafka.producer;

import com.donora.config.KafkaTopicConfig;
import com.donora.dto.kafka.FoodDonationStatusKafkaMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class FoodDonationStatusKafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendFoodDonationStatusUpdate(FoodDonationStatusKafkaMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(KafkaTopicConfig.FOOD_DONATION_STATUS_TOPIC, json);
            System.out.println("📤 Kafka: Food Donation Status Sent → " + json);
        } catch (Exception e) {
            System.err.println("❌ Kafka Send Failed: " + e.getMessage());
        }
    }
}
