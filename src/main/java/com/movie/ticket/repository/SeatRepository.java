package com.movie.ticket.repository;

import com.movie.ticket.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowShowId(Long showId);
    List<Seat> findByShowShowIdAndIsBooked(Long showId, Boolean isBooked);
    List<Seat> findByShowShowIdAndSeatNumberIn(Long showId, List<String> seatNumbers);
}
