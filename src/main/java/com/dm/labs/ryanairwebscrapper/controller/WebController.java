package com.dm.labs.ryanairwebscrapper.controller;

import com.dm.labs.ryanairwebscrapper.entity.Trip;
import com.dm.labs.ryanairwebscrapper.model.Fare;
import com.dm.labs.ryanairwebscrapper.service.FareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WebController {

    private final FareService fareService;

    public WebController(FareService fareService) {
        this.fareService = fareService;
    }

    @Operation(summary = "Generates trip price update")
    @Parameter(in = ParameterIn.PATH, name ="origin" ,schema = @Schema(type = "string"))
    @Parameter(in = ParameterIn.PATH, name ="destination" ,schema = @Schema(type = "string"))
    @Parameter(in = ParameterIn.PATH, name ="date" ,schema = @Schema(type = "date"))
    @PostMapping("/trip/{origin}/{destination}/{date}")
    public List<Fare> createFares(@PathVariable String origin, @PathVariable String destination, @PathVariable String date) {
        var root = fareService.fareByMonth(origin, destination, date);
        fareService.persist(origin, destination, root);
        return root;
    }

    @Operation(summary = "Retrieves price report by day")
    @GetMapping("/cache/{origin}/{destination}/{date}")
    public Trip readTripByDate(@PathVariable String origin, @PathVariable String destination, @PathVariable String date) {
        var root = fareService.cache(origin, destination, date);
        return root;
    }

    @Operation(summary = "Retrieves price report by month")
    @GetMapping("/caches/{origin}/{destination}/{date}")
    public List<Trip> readTripsByMonth(@PathVariable String origin, @PathVariable String destination, @PathVariable String date) {
        var root = fareService.caches(origin, destination, date);
        return root;
    }

    @Operation(summary = "Delete monthly trip report")
    @DeleteMapping("/caches/{origin}/{destination}/{date}")
    public void deleteTripByMonth(@PathVariable String origin, @PathVariable String destination, @PathVariable String date) {
        fareService.delete(origin, destination, date);
    }

}
