package com.donora.kafka.producer;

import com.donora.config.KafkaTopicConfig;
import com.donora.dto.kafka.EmergencyRequestKafkaMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmergencyRequestKafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendEmergencyRequest(EmergencyRequestKafkaMessage message) {
        try {
            System.out.println("🚚 [Producer] Preparing to send emergency request message...");
            String json = objectMapper.writeValueAsString(message);

            System.out.println("📤 [Producer] Sending JSON → " + json);
            kafkaTemplate.send(KafkaTopicConfig.EMERGENCY_BROADCAST_TOPIC, json);
            System.out.println("🚨 Kafka Emergency Request Sent → " + json);
        } catch (Exception e) {
            System.err.println("❌ Failed to send Emergency Kafka message: " + e.getMessage());
        }
    }
}
