package com.donora.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    public static final String ITEM_DONATION_TOPIC = "item.donation.created";
    public static final String FOOD_DONATION_TOPIC = "food.donation.created";
    public static final String ITEM_DONATION_STATUS_TOPIC = "item.donation.status";
    public static final String FOOD_DONATION_STATUS_TOPIC = "food.donation.status"; // ✅ NEW
    public static final String EMERGENCY_BROADCAST_TOPIC = "emergency.request.broadcast";



    @Bean
    public NewTopic itemDonationTopic() {
        return new NewTopic(ITEM_DONATION_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic foodDonationTopic() {
        return new NewTopic(FOOD_DONATION_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic itemDonationStatusTopic() {
        return new NewTopic(ITEM_DONATION_STATUS_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic foodDonationStatusTopic () { // ✅ NEW
        return new NewTopic(FOOD_DONATION_STATUS_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic emergencyBroadcastTopic() {
        return new NewTopic(EMERGENCY_BROADCAST_TOPIC, 1, (short) 1);
    }

}
