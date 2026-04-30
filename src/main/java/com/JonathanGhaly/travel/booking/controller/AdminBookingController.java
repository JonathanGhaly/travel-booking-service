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
@RequestMapping("/api/v1/admin-bookings")
@RequiredArgsConstructor
public class AdminBookingController {
    private final @Qualifier("adminBookingService") BookingService bookingService;

    /** ---------------- ADMIN ENDPOINTS ---------------- **/

    @GetMapping
    public List<BookingResponseDto> getAllBookings() {
        return bookingService.getAll();
    }

    @GetMapping("/by-user")
    public List<BookingResponseDto> getBookingsByUser(@RequestParam UUID userId) {
        return bookingService.getByUserId(userId);
    }

    @GetMapping("/by-poi")
    public List<BookingResponseDto> getBookingsByPoi(@RequestParam UUID poiId) {
        return bookingService.getByPoiId(poiId);
    }

    @GetMapping("/by-user-and-poi")
    public List<BookingResponseDto> getBookingsByUserAndPoi(@RequestParam UUID userId,
                                                            @RequestParam UUID poiId) {
        return bookingService.getByUserIdAndPoiId(userId, poiId);
    }

    @GetMapping("/{id}")
    public BookingResponseDto getBookingById(@PathVariable UUID id) {
        return bookingService.getById(id);
    }

    @PostMapping
    public BookingResponseDto createBookingAdmin(@RequestBody @Valid BookingRequestDto bookingRequestDto) {
        return bookingService.create(bookingRequestDto);
    }

    @PutMapping("/{id}")
    public BookingResponseDto updateBookingAdmin(@PathVariable UUID id,
                                                 @RequestBody @Valid BookingRequestDto bookingRequestDto) {
        return bookingService.update(id, bookingRequestDto);
    }

    @PatchMapping("/{id}")
    public BookingResponseDto patchBookingAdmin(@PathVariable UUID id,
                                                @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.patch(id, bookingRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookingAdmin(@PathVariable UUID id) {
        bookingService.delete(id);
    }
}
