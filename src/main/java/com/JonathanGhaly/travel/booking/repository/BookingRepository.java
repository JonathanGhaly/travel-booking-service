package com.JonathanGhaly.travel.booking.repository;

import com.JonathanGhaly.travel.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByUserId(UUID userId);
    List<Booking> findByPoiId(UUID poiId);
    List<Booking> findByUserIdAndPoiId(UUID userId, UUID poiId);
}
