package com.dm.labs.ryanairwebscrapper.service;

import com.dm.labs.ryanairwebscrapper.entity.Price;
import com.dm.labs.ryanairwebscrapper.entity.Trip;
import com.dm.labs.ryanairwebscrapper.model.Root;
import com.dm.labs.ryanairwebscrapper.repository.PriceRepository;
import com.dm.labs.ryanairwebscrapper.repository.TripRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FareService {

    private final RestTemplate restTemplate;
    private final TripRepository repository;
    private final PriceRepository priceRepository;
    private String url = "https://www.ryanair.com/api/farfnd/v4";

    public FareService(RestTemplateBuilder restTemplateBuilder, TripRepository repository, PriceRepository priceRepository) {
        this.restTemplate = restTemplateBuilder.rootUri(url).build();
        this.repository = repository;
        this.priceRepository = priceRepository;
    }

    public Root fareByMonth(String origin, String destination, String date) {
        if (date.length() == 2) {
            date = LocalDate.of(LocalDate.now().getYear(), Integer.valueOf(date), 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        String fooResourceUrl = String.format("/oneWayFares/%s/%s/cheapestPerDay?outboundMonthOfDate=%s&currency=EUR", origin, destination, date);
        ResponseEntity<Root> response = restTemplate.getForEntity(fooResourceUrl, Root.class);

        return response.getBody();
    }

    public void persist(String origin, String destination, Root root) {
        for (var fare : root.getOutbound().getFares()) {
            Trip t = repository.findByOriginAndDestinationAndDate(origin, destination, fare.getDay());
            if (t == null) {
                t = new Trip();
                t.setOrigin(origin);
                t.setDestination(destination);
                t.setDate(fare.getDay());
            }

            if (fare.getPrice() != null) {
                Price price = new Price();
                price.setPrice(Double.parseDouble(fare.getPrice().getValue()));
                price.setDate(LocalDateTime.now());
                price.setCurrency(fare.getPrice().getCurrencyCode());

                t.getFares().add(price);
                priceRepository.save(price);
            }
            repository.save(t);
        }
    }

    public Trip cache(String origin, String destination, String date) {
        return repository.findByOriginAndDestinationAndDate(origin, destination, date);
    }

    public List<Trip> caches(String origin, String destination, String date) {
        List<Trip> result = new ArrayList<>();
        var firstDay = LocalDate.from(new Date(date).toInstant());
        var LastDay = LocalDate.from(new Date(date).toInstant());

        //return repository.findByOriginAndDestinationAndDate(origin, destination, date);
        return List.of();
    }
}
