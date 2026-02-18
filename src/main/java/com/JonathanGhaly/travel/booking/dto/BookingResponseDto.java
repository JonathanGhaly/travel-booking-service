package com.JonathanGhaly.travel.booking.dto;

import com.JonathanGhaly.travel.booking.enums.BookingStatus;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private UUID id;
    private UUID userId;
    private UUID poiId;
    private BookingStatus status;
    private OffsetDateTime bookingTime;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
