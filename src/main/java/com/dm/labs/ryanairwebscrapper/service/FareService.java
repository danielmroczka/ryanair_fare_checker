package com.dm.labs.ryanairwebscrapper.service;

import com.dm.labs.ryanairwebscrapper.entity.Fare;
import com.dm.labs.ryanairwebscrapper.entity.Trip;
import com.dm.labs.ryanairwebscrapper.model.Root;
import com.dm.labs.ryanairwebscrapper.repository.FareRepository;
import com.dm.labs.ryanairwebscrapper.repository.TripRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FareService {

    private final RestTemplate restTemplate;
    private final TripRepository tripRepository;
    private final FareRepository fareRepository;
    private final String url = "https://www.ryanair.com/api/farfnd/v4";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FareService(RestTemplateBuilder restTemplateBuilder, TripRepository tripRepository, FareRepository priceRepository) {
        this.restTemplate = restTemplateBuilder.rootUri(url).build();
        this.tripRepository = tripRepository;
        this.fareRepository = priceRepository;
    }

    public List<com.dm.labs.ryanairwebscrapper.model.Fare> fareByMonth(String origin, String destination, String date) {
        List<com.dm.labs.ryanairwebscrapper.model.Fare> fares = new ArrayList<>();

        String fooResourceUrl = String.format("/oneWayFares/%s/%s/cheapestPerDay?outboundMonthOfDate=%s&currency=EUR", origin, destination, toFirstMonthDay(date));
        ResponseEntity<Root> response = restTemplate.getForEntity(fooResourceUrl, Root.class);
        fares.addAll(response.getBody().getOutbound().getFares().stream().filter(fare -> fare.getPrice() != null).toList());

        return fares;
    }

    public void persist(String origin, String destination, List<com.dm.labs.ryanairwebscrapper.model.Fare> fares) {
        for (var fare : fares) {
            Trip trip = fetchTrip(origin, destination, fare);

            if (fare.getPrice() != null) {
                Fare price = new Fare();
                price.setPrice(Double.parseDouble(fare.getPrice().getValue()));
                price.setDate(LocalDateTime.now());
                price.setCurrency(fare.getPrice().getCurrencyCode());
                trip.getFares().add(price);

                fareRepository.save(price);
            }

            tripRepository.save(trip);

        }
    }

    private Trip fetchTrip(String origin, String destination, com.dm.labs.ryanairwebscrapper.model.Fare fare) {
        Trip trip = tripRepository.findByOriginAndDestinationAndDate(origin, destination, LocalDate.parse(fare.getDay()));
        if (trip == null) {
            trip = new Trip();
            trip.setOrigin(origin);
            trip.setDestination(destination);
            trip.setDate(LocalDate.parse(fare.getDay()));
            tripRepository.save(trip);
        }
        return trip;
    }

    public Trip cache(String origin, String destination, String date) {
        return tripRepository.findByOriginAndDestinationAndDate(origin, destination, LocalDate.parse(date));
    }

    public List<Trip> caches(String origin, String destination, String date) {
        YearMonth yearMonth = YearMonth.parse(date);
        return tripRepository.findByOriginAndDestinationAndDateBetween(origin, destination, yearMonth.atDay(1), yearMonth.atEndOfMonth());
    }

    public void delete(String origin, String destination, String date) {
        tripRepository.deleteAllInBatch(caches(origin, destination, date));
    }

    private LocalDate toFirstMonthDay(String date) {
        YearMonth month = YearMonth.parse(date);
        return month.atDay(1);
    }
}
