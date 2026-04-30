package com.JonathanGhaly.travel.booking.kafka.publisher;

import com.JonathanGhaly.travel.booking.dto.BookingEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic = "booking-events"; // your Kafka topic

    public void publish(BookingEventDto event) {
        kafkaTemplate.send(topic, event.getBookingId().toString(), event);
    }
}
