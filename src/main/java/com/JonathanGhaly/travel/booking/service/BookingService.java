package com.JonathanGhaly.travel.booking.service;

import com.JonathanGhaly.travel.booking.dto.BookingRequestDto;
import com.JonathanGhaly.travel.booking.dto.BookingResponseDto;

import java.util.List;
import java.util.UUID;

public interface BookingService {

    BookingResponseDto create(BookingRequestDto bookingRequestDto);
    BookingResponseDto update(UUID id, BookingRequestDto bookingRequestDto);
    BookingResponseDto patch(UUID id, BookingRequestDto bookingRequestDto);

    BookingResponseDto getById(UUID id);
    List<BookingResponseDto> getAll();
    List<BookingResponseDto> getByUserId(UUID userId);
    List<BookingResponseDto> getByPoiId(UUID poiId);
    List<BookingResponseDto> getByUserIdAndPoiId(UUID userId, UUID poiId);

    void delete(UUID id);
}
