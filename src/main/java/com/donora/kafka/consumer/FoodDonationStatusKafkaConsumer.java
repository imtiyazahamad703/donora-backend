package com.donora.kafka.consumer;

import com.donora.dto.kafka.FoodDonationStatusKafkaMessage;
import com.donora.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FoodDonationStatusKafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "food.donation.status", groupId = "donora-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            String json = record.value();
            FoodDonationStatusKafkaMessage message = objectMapper.readValue(json, FoodDonationStatusKafkaMessage.class);

            // Console log
            System.out.println("📢 Food Donation Status Update:");
            System.out.println("📨 Donor: " + message.getDonorEmail());
            System.out.println("🍱 Food: " + message.getFoodName() + " x" + message.getQuantity() + " → " + message.getNewStatus());

            // Send Email
            String subject = "Your Food Donation Was " + message.getNewStatus();
            String body = String.format(
                    "Dear Donor,\n\nThank you for your contribution.\n\n" +
                            "Your donation of %s (Quantity: %d) has been %s by the NGO.\n\n" +
                            "We appreciate your kindness!\n\n- Donora Team",
                    message.getFoodName(), message.getQuantity(), message.getNewStatus()
            );

            emailService.sendEmail(message.getDonorEmail(), subject, body);

        } catch (Exception e) {
            System.err.println("❌ Kafka Food Status Message Error: " + e.getMessage());
        }
    }
}
