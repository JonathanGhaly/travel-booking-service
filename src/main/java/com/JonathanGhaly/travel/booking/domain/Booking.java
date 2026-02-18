package com.JonathanGhaly.travel.booking.domain;

import com.JonathanGhaly.travel.booking.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="bookings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @Column(name="uuid",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id",nullable = false)
    private UUID userId;

    @Column(name = "poi_id",nullable = false)
    private UUID poiId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Column(name = "booking_time",nullable = false)
    private OffsetDateTime bookingTime;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) id = UUID.randomUUID();
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

}
