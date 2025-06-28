package com.donora.kafka.consumer;

import com.donora.dto.kafka.ItemDonationKafkaMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ItemDonationKafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "item.donation.created", groupId = "donora-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            String value = record.value();
            ItemDonationKafkaMessage message = objectMapper.readValue(value, ItemDonationKafkaMessage.class);

            // Simulate real-time alert (later we will send email/websocket)
            System.out.println(" [New Donation Alert]");
            System.out.println(" Donor Email: " + message.getDonorEmail());
            System.out.println(" Item: " + message.getItemName() + " x" + message.getQuantity());
            System.out.println(" For NGO: " + message.getNgoEmail());
        } catch (Exception e) {
            System.err.println("Failed to process Kafka message: " + e.getMessage());
        }
    }
}
