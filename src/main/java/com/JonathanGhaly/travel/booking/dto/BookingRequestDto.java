package com.JonathanGhaly.travel.booking.dto;

import com.JonathanGhaly.travel.booking.enums.BookingStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "POI ID is required")
    private UUID poiId;

    BookingStatus bookingStatus;

    @NotNull(message = "Booking time is required")
    @Future(message = "Booking time must be in the future")
    private OffsetDateTime bookingTime;
}
