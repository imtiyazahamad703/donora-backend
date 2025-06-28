package com.donora.kafka.consumer;

import com.donora.dto.kafka.ItemDonationStatusKafkaMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ItemDonationStatusKafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "item.donation.status", groupId = "donora-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            String json = record.value();
            ItemDonationStatusKafkaMessage message = objectMapper.readValue(json, ItemDonationStatusKafkaMessage.class);

            System.out.println("📢 Donation Status Update:");
            System.out.println("📨 Donor: " + message.getDonorEmail());
            System.out.println("📦 Item: " + message.getItemName() + " x" + message.getQuantity() + " → ✅ " + message.getNewStatus());
        } catch (Exception e) {
            System.err.println("❌ Failed to process donation status update message: " + e.getMessage());
        }
    }
}
