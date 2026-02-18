package com.JonathanGhaly.travel.booking.mapper;

import com.JonathanGhaly.travel.booking.domain.Booking;
import com.JonathanGhaly.travel.booking.dto.BookingRequestDto;
import com.JonathanGhaly.travel.booking.dto.BookingResponseDto;
import com.JonathanGhaly.travel.booking.enums.BookingStatus;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class BookingMapper {
    public Booking toEntity (BookingRequestDto bookingRequestDto) {
        return Booking.builder()
                .id(UUID.randomUUID())
                .userId(bookingRequestDto.getUserId())
                .poiId(bookingRequestDto.getPoiId())
                .bookingTime(bookingRequestDto.getBookingTime())
                .status(BookingStatus.PENDING)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
    public BookingResponseDto toDto (Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .userId(booking.getUserId())
                .poiId(booking.getPoiId())
                .status(booking.getStatus())
                .bookingTime(booking.getBookingTime())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
