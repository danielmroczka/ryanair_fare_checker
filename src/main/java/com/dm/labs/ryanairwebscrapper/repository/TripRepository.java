package com.dm.labs.ryanairwebscrapper.repository;

import com.dm.labs.ryanairwebscrapper.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Trip findByOriginAndDestinationAndDate(String origin, String destination, String date);
}
