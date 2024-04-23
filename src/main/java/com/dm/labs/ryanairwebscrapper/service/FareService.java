package com.dm.labs.ryanairwebscrapper.service;

import com.dm.labs.ryanairwebscrapper.entity.Price;
import com.dm.labs.ryanairwebscrapper.entity.Trip;
import com.dm.labs.ryanairwebscrapper.model.Fare;
import com.dm.labs.ryanairwebscrapper.model.Root;
import com.dm.labs.ryanairwebscrapper.repository.PriceRepository;
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
    private final PriceRepository priceRepository;
    private final String url = "https://www.ryanair.com/api/farfnd/v4";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FareService(RestTemplateBuilder restTemplateBuilder, TripRepository tripRepository, PriceRepository priceRepository) {
        this.restTemplate = restTemplateBuilder.rootUri(url).build();
        this.tripRepository = tripRepository;
        this.priceRepository = priceRepository;
    }

    public List<Fare> fareByMonth(String origin, String destination, String date) {
        List<Fare> fares = new ArrayList<>();

        String fooResourceUrl = String.format("/oneWayFares/%s/%s/cheapestPerDay?outboundMonthOfDate=%s&currency=EUR", origin, destination, toFirstMonthDay(date));
        ResponseEntity<Root> response = restTemplate.getForEntity(fooResourceUrl, Root.class);
        fares.addAll(response.getBody().getOutbound().getFares().stream().filter(fare -> fare.getPrice() != null).toList());

        return fares;
    }

    public void persist(String origin, String destination, List<Fare> fares) {
        for (var fare : fares) {
            Trip t = tripRepository.findByOriginAndDestinationAndDate(origin, destination, LocalDate.parse(fare.getDay()));
            if (t == null) {
                t = new Trip();
                t.setOrigin(origin);
                t.setDestination(destination);
                t.setDate(LocalDate.parse(fare.getDay()));
            }

            if (fare.getPrice() != null) {
                Price price = new Price();
                price.setPrice(Double.parseDouble(fare.getPrice().getValue()));
                price.setDate(LocalDateTime.now());
                price.setCurrency(fare.getPrice().getCurrencyCode());

                t.getFares().add(price);
                priceRepository.save(price);
                tripRepository.save(t);
            }

        }
    }

    public Trip cache(String origin, String destination, String date) {
        return tripRepository.findByOriginAndDestinationAndDate(origin, destination, toFirstMonthDay(date));
    }

    public List<Trip> caches(String origin, String destination, String date) {
        YearMonth yearMonth = YearMonth.from(LocalDate.parse(date));
        return tripRepository.findByOriginAndDestinationAndDateBetween(origin, destination, yearMonth.atDay(1), yearMonth.atEndOfMonth());    }

    public void delete(String origin, String destination, String date) {
        tripRepository.deleteAllInBatch(caches(origin, destination, date));
    }

    private LocalDate toFirstMonthDay(String date) {
        YearMonth month = YearMonth.parse(date);
        return month.atDay(1);
    }
}
