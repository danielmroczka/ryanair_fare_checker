package com.dm.labs.ryanairwebscrapper.repository;

import com.dm.labs.ryanairwebscrapper.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Trip findByOriginAndDestinationAndDate(String origin, String destination, LocalDate date);

    List<Trip> findByOriginAndDestinationAndDateBetween(String origin, String destination, LocalDate dateStart, LocalDate dateEnd);
}
