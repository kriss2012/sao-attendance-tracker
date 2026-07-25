package com.movie.ticket.repository;

import com.movie.ticket.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserUserIdOrderByBookingDateTimeDesc(Long userId);
}
