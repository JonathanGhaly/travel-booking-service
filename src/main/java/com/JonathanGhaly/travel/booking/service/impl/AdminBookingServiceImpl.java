package com.JonathanGhaly.travel.booking.service.impl;

import com.JonathanGhaly.travel.booking.domain.Booking;
import com.JonathanGhaly.travel.booking.dto.BookingEventDto;
import com.JonathanGhaly.travel.booking.dto.BookingRequestDto;
import com.JonathanGhaly.travel.booking.dto.BookingResponseDto;
import com.JonathanGhaly.travel.booking.exception.ResourceNotFoundException;
import com.JonathanGhaly.travel.booking.kafka.publisher.BookingEventPublisher;
import com.JonathanGhaly.travel.booking.mapper.BookingMapper;
import com.JonathanGhaly.travel.booking.repository.BookingRepository;
import com.JonathanGhaly.travel.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Qualifier("adminBookingService")
public class AdminBookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper mapper;
    private final BookingEventPublisher bookingPublisher;

    @Override
    @CacheEvict(value = "bookings", allEntries = true)
    public BookingResponseDto create(BookingRequestDto bookingRequestDto) {
        Booking entity = mapper.toEntity(bookingRequestDto);
        Booking saved = bookingRepository.save(entity);

        bookingPublisher.publish(new BookingEventDto(
                saved.getId(),
                saved.getUserId(),
                saved.getPoiId(),
                "CREATED",
                System.currentTimeMillis()
        ));
        return mapper.toDto(saved);
    }

    @Override
    @CacheEvict(value = "bookings", allEntries = true)
    public BookingResponseDto update(UUID id, BookingRequestDto bookingRequestDto) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found with id: " + id));

        existing.setUserId(bookingRequestDto.getUserId());
        existing.setPoiId(bookingRequestDto.getPoiId());
        existing.setStatus(bookingRequestDto.getBookingStatus());
        existing.setBookingTime(bookingRequestDto.getBookingTime());
        Booking saved = bookingRepository.save(existing);

        bookingPublisher.publish(new BookingEventDto(
                saved.getId(),
                saved.getUserId(),
                saved.getPoiId(),
                "UPDATED",
                System.currentTimeMillis()
        ));
        return mapper.toDto(saved);
    }

    @Override
    @CacheEvict(value = {"booking-user", "booking-poi", "booking-single"}, allEntries = true)
    public BookingResponseDto patch(UUID id, BookingRequestDto bookingRequestDto) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found with id: " + id));
        if(bookingRequestDto.getUserId()!= null){
            existing.setUserId(bookingRequestDto.getUserId());
        }
        if(bookingRequestDto.getPoiId()!= null){
            existing.setPoiId(bookingRequestDto.getPoiId());
        }
        if(bookingRequestDto.getBookingStatus()!= null){
            existing.setStatus(bookingRequestDto.getBookingStatus());
        }
        if (bookingRequestDto.getBookingTime()!= null){
            existing.setBookingTime(bookingRequestDto.getBookingTime());
        }
        Booking saved = bookingRepository.save(existing);

        bookingPublisher.publish(new BookingEventDto(
                saved.getId(),
                saved.getUserId(),
                saved.getPoiId(),
                "UPDATED",
                System.currentTimeMillis()
        ));
        return mapper.toDto(saved);
    }

    @Override
    @Cacheable(value = "booking-single", key = "#id")
    public BookingResponseDto getById(UUID id) {
        Booking entity = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return mapper.toDto(entity);
    }

    @Override
    public List<BookingResponseDto> getAll() {
        return bookingRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Cacheable(value = "booking-user", key="#userId")
    public List<BookingResponseDto> getByUserId(UUID userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Cacheable(value = "booking-poi",key = "#poiId")
    public List<BookingResponseDto> getByPoiId(UUID poiId) {
        return bookingRepository.findByPoiId(poiId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getByUserIdAndPoiId(UUID userId, UUID poiId) {
        return bookingRepository.findByUserIdAndPoiId(userId,poiId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @CacheEvict(value = {"booking-user", "booking-poi", "booking-single"}, allEntries = true)
    public void delete(UUID id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Booking not found with id: " + id));
        bookingRepository.deleteById(id);
        bookingPublisher.publish(new BookingEventDto(
                booking.getId(),
                booking.getUserId(),
                booking.getPoiId(),
                "DELETED",
                System.currentTimeMillis()
        ));
    }

}
