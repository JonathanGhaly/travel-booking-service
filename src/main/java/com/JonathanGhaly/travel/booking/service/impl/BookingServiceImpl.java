package com.JonathanGhaly.travel.booking.service.impl;

import com.JonathanGhaly.travel.booking.domain.Booking;
import com.JonathanGhaly.travel.booking.dto.BookingRequestDto;
import com.JonathanGhaly.travel.booking.dto.BookingResponseDto;
import com.JonathanGhaly.travel.booking.exception.ResourceNotFoundException;
import com.JonathanGhaly.travel.booking.mapper.BookingMapper;
import com.JonathanGhaly.travel.booking.repository.BookingRepository;
import com.JonathanGhaly.travel.booking.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper mapper;

    @Override
    @CacheEvict(value = "bookings", allEntries = true)
    public BookingResponseDto create(BookingRequestDto bookingRequestDto) {
        Booking entity = mapper.toEntity(bookingRequestDto);
        return mapper.toDto(bookingRepository.save(entity));
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

        return mapper.toDto(bookingRepository.save(existing));
    }

    @Override
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

        return mapper.toDto(bookingRepository.save(existing));
    }

    @Override
    @Cacheable(value = "bookings",key = "#id")
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
    @Cacheable(value = "bookings", key="#userId")
    public List<BookingResponseDto> getByUserId(UUID userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Cacheable(value = "bookings",key = "#poiId")
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
    @CacheEvict(value = "bookings", allEntries = true)
    public void delete(UUID id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Booking not found with id: " + id));
        bookingRepository.deleteById(id);
    }
}
