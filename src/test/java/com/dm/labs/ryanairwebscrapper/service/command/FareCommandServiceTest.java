package com.dm.labs.ryanairwebscrapper.service.command;

import com.dm.labs.ryanairwebscrapper.RyanairWebscrapperApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = RyanairWebscrapperApplication.class)
class FareCommandServiceTest {

    @Autowired
    private FareCommandService service;

    @Test
    public void checkFareMonthAhead() {
        LocalDate localDate = LocalDate.now();
        localDate.plusMonths(1);
        var month = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        var result = service.fareByMonth("KRK", "VIE", month);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void checkFareMonthAgo() {
        LocalDate localDate = LocalDate.now();
        var month = localDate.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));

        var result = service.fareByMonth("KRK", "VIE", month);
        assertTrue(result.isEmpty());
    }

}