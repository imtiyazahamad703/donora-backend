package com.donora.kafka.consumer;

import com.donora.dto.kafka.FoodDonationKafkaMessage;
import com.donora.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FoodDonationKafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "food.donation.created", groupId = "donora-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            String json = record.value();
            FoodDonationKafkaMessage message = objectMapper.readValue(json, FoodDonationKafkaMessage.class);

            // ✅ Console Notification
            System.out.println("📦 [New Food Donation Alert]");
            System.out.println("🥘 Donor Email: " + message.getDonorEmail());
            System.out.println("🍱 Food: " + message.getFoodName() + " x" + message.getQuantity());
            System.out.println("📍 For NGO: " + message.getNgoEmail());

            // ✅ Email Notification
            String subject = "🍱 New Food Donation Received";
            String body = String.format(
                    "Dear NGO,\n\nYou have received a new food donation.\n\nItem: %s\nQuantity: %d\nDonor: %s\n\nPlease log in to your dashboard to view more details.\n\n- Donora Team",
                    message.getFoodName(),
                    message.getQuantity(),
                    message.getDonorEmail()
            );

            emailService.sendEmail(message.getNgoEmail(), subject, body);

        } catch (Exception e) {
            System.err.println("❌ Kafka Food Donation Consumer Error: " + e.getMessage());
        }
    }
}
