package com.donora.kafka.consumer;

import com.donora.dto.kafka.ItemDonationKafkaMessage;
import com.donora.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ItemDonationKafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private EmailService emailService; // ✅ Inject your email service

    @KafkaListener(topics = "item.donation.created", groupId = "donora-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            String value = record.value();
            ItemDonationKafkaMessage message = objectMapper.readValue(value, ItemDonationKafkaMessage.class);

            System.out.println("📦 [New Donation Alert]");
            System.out.println("📨 Donor Email: " + message.getDonorEmail());
            System.out.println("🎁 Item: " + message.getItemName() + " x" + message.getQuantity());
            System.out.println("📍 For NGO: " + message.getNgoEmail());

            // ✅ Send email
            String subject = "📦 New Donation Received";
            String body = String.format(
                    "You have received a new donation!\n\nDonor: %s\nItem: %s\nQuantity: %d",
                    message.getDonorEmail(),
                    message.getItemName(),
                    message.getQuantity()
            );
            emailService.sendEmail(message.getNgoEmail(), subject, body);

        } catch (Exception e) {
            System.err.println("❌ Failed to process Kafka message: " + e.getMessage());
        }
    }
}
