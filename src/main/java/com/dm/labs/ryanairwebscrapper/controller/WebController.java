package com.dm.labs.ryanairwebscrapper.controller;

import com.dm.labs.ryanairwebscrapper.entity.Trip;
import com.dm.labs.ryanairwebscrapper.model.Fare;
import com.dm.labs.ryanairwebscrapper.service.FareService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WebController {

    private final FareService fareService;

    public WebController(FareService fareService) {
        this.fareService = fareService;
    }

    @GetMapping("/trip/{origin}/{destination}/{date}")
    public List<Fare> getTrip(@PathVariable String origin, @PathVariable String destination, @PathVariable String date) {
        var root = fareService.fareByMonth(origin, destination, date);
        fareService.persist(origin, destination, root);
        return root;
    }

    @GetMapping("/cache/{origin}/{destination}/{date}")
    public Trip readFromDB(@PathVariable String origin, @PathVariable String destination, @PathVariable String date) {
        var root = fareService.cache(origin, destination, date);
        return root;
    }

    @GetMapping("/caches/{origin}/{destination}/{date}")
    public List<Trip> readFromDBS(@PathVariable String origin, @PathVariable String destination, @PathVariable String date) {
        var root = fareService.caches(origin, destination, date);
        return root;
    }

    @DeleteMapping("/caches/{origin}/{destination}/{date}")
    public void deleteTripByMonth(@PathVariable String origin, @PathVariable String destination, @PathVariable String date) {
        fareService.delete(origin, destination, date);
    }

}
