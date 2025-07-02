package com.donora.kafka.consumer;

import com.donora.dto.kafka.ItemDonationStatusKafkaMessage;
import com.donora.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ItemDonationStatusKafkaConsumer {

    @Autowired
    private EmailService emailService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "item.donation.status", groupId = "donora-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            String json = record.value();
            ItemDonationStatusKafkaMessage message = objectMapper.readValue(json, ItemDonationStatusKafkaMessage.class);

            // ✅ Console Logging
            System.out.println("📢 Item Donation Status Update:");
            System.out.println("📨 Donor: " + message.getDonorEmail());
            System.out.println("📦 Item: " + message.getItemName() + " x" + message.getQuantity() + " → " + message.getNewStatus());

            // ✅ Send Email Notification
            String subject = "Update on Your Item Donation";
            String body = String.format(
                    "Hello,\n\nYour donated item '%s' (Quantity: %d) has been %s by the NGO.\n\nThank you for your support!\n\n– Donora Team",
                    message.getItemName(),
                    message.getQuantity(),
                    message.getNewStatus()
            );

            emailService.sendEmail(message.getDonorEmail(), subject, body);

        } catch (Exception e) {
            System.err.println("❌ Kafka Item Status Message Error: " + e.getMessage());
        }
    }
}
