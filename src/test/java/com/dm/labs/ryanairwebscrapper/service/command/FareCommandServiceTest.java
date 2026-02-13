package com.dm.labs.ryanairwebscrapper.service.command;

import com.dm.labs.ryanairwebscrapper.clientmodel.Fare;
import com.dm.labs.ryanairwebscrapper.clientmodel.Outbound;
import com.dm.labs.ryanairwebscrapper.clientmodel.Root;
import com.dm.labs.ryanairwebscrapper.repository.FareRepository;
import com.dm.labs.ryanairwebscrapper.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FareCommandServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private FareRepository fareRepository;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    private FareCommandService service;

    @BeforeEach
    public void init() {
        // Set up the RestTemplateBuilder mock to handle method calls
        when(restTemplateBuilder.rootUri(anyString())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        service = new FareCommandService(
                "http://localhost:8080",
                restTemplateBuilder,
                tripRepository,
                fareRepository
        );
    }

    private static Root getRoot() {
        Root mockRoot = new Root();
        Outbound outbound = new Outbound();
        List<Fare> fareList = new ArrayList<>();

        // Add a fare with price
        Fare fare = new Fare();
        com.dm.labs.ryanairwebscrapper.clientmodel.Price price = new com.dm.labs.ryanairwebscrapper.clientmodel.Price();
        price.setValue("100.0");
        price.setCurrencyCode("EUR");
        fare.setPrice(price);
        fareList.add(fare);

        outbound.setFares(fareList);
        mockRoot.setOutbound(outbound);
        return mockRoot;
    }

    @Test
    public void checkFareMonthAhead() {
        // Setup
        LocalDate localDate = LocalDate.now();
        LocalDate futureDate = localDate.plusMonths(1); // Note: LocalDate is immutable, need to assign result
        String month = futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // Create mock response
        Root mockRoot = getRoot();

        // Mock the REST call
        String expectedUrl = String.format("/oneWayFares/%s/%s/cheapestPerDay?outboundMonthOfDate=%s&currency=EUR",
                "KRK", "VIE", futureDate.withDayOfMonth(1));
        when(restTemplate.getForEntity(anyString(), eq(Root.class)))
                .thenReturn(ResponseEntity.ok(mockRoot));

        // Execute
        var result = service.fareByMonth("KRK", "VIE", month);

        // Verify
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void checkFareMonthAgo() {
        // Setup
        LocalDate localDate = LocalDate.now();
        String month = localDate.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // Create mock response with empty fares
        Root mockRoot = new Root();
        Outbound outbound = new Outbound();
        outbound.setFares(new ArrayList<>());
        mockRoot.setOutbound(outbound);

        // Mock the REST call
        when(restTemplate.getForEntity(anyString(), eq(Root.class)))
                .thenReturn(ResponseEntity.ok(mockRoot));

        // Execute
        var result = service.fareByMonth("KRK", "VIE", month);

        // Verify
        assertTrue(result.isEmpty());
    }

}
