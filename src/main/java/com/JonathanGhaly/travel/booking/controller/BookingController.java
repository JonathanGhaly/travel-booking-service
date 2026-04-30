package com.JonathanGhaly.travel.booking.controller;

import com.JonathanGhaly.travel.booking.dto.BookingRequestDto;
import com.JonathanGhaly.travel.booking.dto.BookingResponseDto;
import com.JonathanGhaly.travel.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final @Qualifier("userBookingService") BookingService bookingService;


    @GetMapping("/user")
    public List<BookingResponseDto> getMyBookings() {
        return bookingService.getAll();
    }

    @GetMapping("/user/{id}")
    public BookingResponseDto getMyBookingById(@PathVariable UUID id) {
        return bookingService.getById(id);
    }

    @GetMapping("user/by-poi")
    public List<BookingResponseDto> getMyBookingsByPoi(@RequestParam UUID poiId) {
        return bookingService.getByPoiId(poiId);
    }

    @PostMapping("/user")
    public BookingResponseDto createBooking(@RequestBody @Valid BookingRequestDto bookingRequestDto) {
        return bookingService.create(bookingRequestDto);
    }

    @PutMapping("/user/{id}")
    public BookingResponseDto updateBooking(@PathVariable UUID id,
                                            @RequestBody @Valid BookingRequestDto bookingRequestDto) {
        return bookingService.update(id, bookingRequestDto);
    }

    @PatchMapping("/user/{id}")
    public BookingResponseDto patchBooking(@PathVariable UUID id,
                                           @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.patch(id, bookingRequestDto);
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable UUID id) {
        bookingService.delete(id);
    }

}