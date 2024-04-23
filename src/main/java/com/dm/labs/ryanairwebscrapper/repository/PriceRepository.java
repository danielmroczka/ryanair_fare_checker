package com.dm.labs.ryanairwebscrapper.repository;

import com.dm.labs.ryanairwebscrapper.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long> {
}
