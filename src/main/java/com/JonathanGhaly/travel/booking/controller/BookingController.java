package com.JonathanGhaly.travel.booking.controller;

import com.JonathanGhaly.travel.booking.dto.BookingRequestDto;
import com.JonathanGhaly.travel.booking.dto.BookingResponseDto;
import com.JonathanGhaly.travel.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingResponseDto> getBookings(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID poiId) {

        if (userId != null && poiId != null) {
            return bookingService.getByUserIdAndPoiId(userId, poiId);
        }

        if (userId != null) {
            return bookingService.getByUserId(userId);
        }

        if (poiId != null) {
            return bookingService.getByPoiId(poiId);
        }

        return bookingService.getAll();
    }
    @GetMapping("/{id}")
    public BookingResponseDto findById(@PathVariable UUID id) {
        return bookingService.getById(id);
    }
    @PostMapping
    public BookingResponseDto create(@RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.create(bookingRequestDto);
    }
    @PutMapping("/{id}")
    public BookingResponseDto update(@PathVariable UUID id, @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.update(id, bookingRequestDto);
    }
    @PatchMapping("/{id}")
    public BookingResponseDto patch(@PathVariable UUID id, @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.patch(id, bookingRequestDto);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        bookingService.delete(id);
    }
}


