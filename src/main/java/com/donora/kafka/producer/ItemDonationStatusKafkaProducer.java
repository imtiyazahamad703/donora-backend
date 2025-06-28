package com.donora.kafka.producer;

import com.donora.config.KafkaTopicConfig;
import com.donora.dto.kafka.ItemDonationStatusKafkaMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ItemDonationStatusKafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendItemDonationStatusUpdate(ItemDonationStatusKafkaMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(KafkaTopicConfig.ITEM_DONATION_STATUS_TOPIC, json);
            System.out.println("📤 Kafka Status Update Sent → " + json);
        } catch (Exception e) {
            System.err.println("❌ Failed to send Kafka status update: " + e.getMessage());
        }
    }

}
