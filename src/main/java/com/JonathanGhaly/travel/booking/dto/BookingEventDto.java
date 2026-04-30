package com.JonathanGhaly.travel.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingEventDto {
    private UUID bookingId;
    private UUID userId;
    private UUID poiId;
    private String status; // e.g., CREATED, UPDATED, DELETED
    private long timestamp;
}