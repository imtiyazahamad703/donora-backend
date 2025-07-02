package com.donora.kafka.consumer;

import com.donora.dto.kafka.EmergencyRequestKafkaMessage;
import com.donora.entity.User;
import com.donora.enums.Role;
import com.donora.repository.UserRepository;
import com.donora.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmergencyRequestKafkaConsumer {

    private final ObjectMapper mapper = new ObjectMapper();
    private final EmailService emailService;
    private final UserRepository userRepository;


    public EmergencyRequestKafkaConsumer(EmailService emailService, UserRepository userRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "emergency.request.broadcast", groupId = "donora-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            String json = record.value();
            EmergencyRequestKafkaMessage message = mapper.readValue(json, EmergencyRequestKafkaMessage.class);

            System.out.println("🚨 EMERGENCY ITEM NEED ALERT 🚨");
            System.out.println("📧 NGO: " + message.getNgoEmail());
            System.out.println("📦 Item: " + message.getItemName() + " x" + message.getQuantity());
            System.out.println("⚠️ Urgency: " + message.getUrgencyLevel());
            System.out.println("📝 Description: " + message.getDescription());

            // ✅ SEND EMAIL BROADCAST TO USERS
            String subject = "🚨 Emergency Request from NGO";
            String body = String.format("""
                    Urgent request from %s

                    📦 Item: %s
                    📏 Quantity: %d
                    ⚠️ Urgency: %s

                    📝 Description: %s
                    """,
                    message.getNgoEmail(),
                    message.getItemName(),
                    message.getQuantity(),
                    message.getUrgencyLevel(),
                    message.getDescription()
            );

            // 🔁 You should ideally fetch all users/business emails here
            // ✅ Send to all USERS + BUSINESS
            List<User> allUsers = userRepository.findByRoleIn(List.of(Role.USER, Role.BUSINESS));
            for (User user : allUsers) {
                emailService.sendEmail(user.getEmail(), subject, body);
                System.out.println("📧 Email sent to: " + user.getEmail());
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to process emergency request: " + e.getMessage());
        }
    }
}
