package com.dm.labs.ryanairwebscrapper.service;

import com.dm.labs.ryanairwebscrapper.entity.Fare;
import com.dm.labs.ryanairwebscrapper.entity.Trip;
import com.dm.labs.ryanairwebscrapper.model.Root;
import com.dm.labs.ryanairwebscrapper.repository.FareRepository;
import com.dm.labs.ryanairwebscrapper.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        fares.addAll(Objects.requireNonNull(response.getBody()).getOutbound().getFares().stream().filter(fare -> fare.getPrice() != null).toList());

        return fares;
    }

    public void saveFares(String origin, String destination, List<com.dm.labs.ryanairwebscrapper.model.Fare> fares) {
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
        Optional<Trip> result = tripRepository.findByOriginAndDestinationAndDate(origin, destination, LocalDate.parse(fare.getDay()));
        if (result.isEmpty() ) {
            var trip = new Trip();
            trip.setOrigin(origin);
            trip.setDestination(destination);
            trip.setDate(LocalDate.parse(fare.getDay()));
            tripRepository.save(trip);
            result = Optional.of(trip);
        }
        return result.get();
    }

    public Trip cache(String origin, String destination, String date) {
        Trip trip = tripRepository.findByOriginAndDestinationAndDate(origin, destination, LocalDate.parse(date)).orElseThrow(EntityNotFoundException::new);

        var last = trip.getFares().get(0).getPrice();

        for (Fare fare : trip.getFares()) {
            var delta = fare.getPrice() - last;
            fare.setChange(delta);
        }
        var min = trip.getFares().stream().mapToDouble(Fare::getPrice).min().getAsDouble();
        var max = trip.getFares().stream().mapToDouble(Fare::getPrice).max().getAsDouble();

        trip.setMin(min);
        trip.setMax(max);

        return trip;
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

    @Scheduled(cron = "0 */1 * ? * *")
    public void scheduleTaskUsingCronExpression() {
        var res = fareByMonth("VIE", "KRK", "2024-05");

        System.out.println(res.toString());
    }
}
