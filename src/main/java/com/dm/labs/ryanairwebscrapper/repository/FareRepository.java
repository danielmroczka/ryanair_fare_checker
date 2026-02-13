package com.dm.labs.ryanairwebscrapper.repository;

import com.dm.labs.ryanairwebscrapper.entity.Fare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FareRepository extends JpaRepository<Fare, Long> {
}
