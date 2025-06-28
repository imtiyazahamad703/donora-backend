package com.donora.kafka.consumer;

import com.donora.dto.kafka.FoodDonationKafkaMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FoodDonationKafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "food.donation.created", groupId = "donora-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            FoodDonationKafkaMessage message = objectMapper.readValue(record.value(), FoodDonationKafkaMessage.class);
            System.out.println("📦 [New Food Donation Alert]");
            System.out.println("🥘 Donor Email: " + message.getDonorEmail());
            System.out.println("🍱 Food: " + message.getFoodName() + " x" + message.getQuantity());
            System.out.println("📍 For NGO: " + message.getNgoEmail());
        } catch (Exception e) {
            System.err.println("❌ Failed to consume Kafka food donation: " + e.getMessage());
        }
    }
}
