package com.dm.labs.ryanairwebscrapper;

import com.dm.labs.ryanairwebscrapper.entity.Quote;
import com.dm.labs.ryanairwebscrapper.model.Fare;
import com.dm.labs.ryanairwebscrapper.model.Root;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class WebController {
    @GetMapping
    @RequestMapping("/2")
    public String hello() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String s = "";
        ResponseEntity<String> result =
                restTemplate.exchange("https://www.ryanair.com/api/booking/v9/pl-pl/availability?ADT=1&CHD=1&DateIn=2024-04-20&DateOut=2024-04-20&Destination=KRK&Disc=0&INF=0&Origin=VIE&TEEN=0&ToUs=AGREED&IncludeConnectingFlights=false&RoundTrip=false", HttpMethod.GET, entity, String.class);
        return result.getBody();
    }

    @GetMapping
    @RequestMapping("/1")
    public ResponseEntity<String> hi() {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "https://www.ryanair.com/api/booking/v9/pl-pl/availability?ADT=1&CHD=1&DateIn=2024-04-20&DateOut=2024-04-20&Destination=KRK&Disc=0&INF=0&Origin=VIE&TEEN=0&ToUs=AGREED&IncludeConnectingFlights=false&RoundTrip=false";
        ResponseEntity<String> response
                = restTemplate.getForEntity(fooResourceUrl, String.class);
        return response;
    }

    @GetMapping
    @RequestMapping("/{origin}/{destination}/{date}")
    public List<Fare> gets(@PathVariable String origin, @PathVariable String destination, @PathVariable String date) {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = String.format( "https://www.ryanair.com/api/farfnd/v4/oneWayFares/%s/%s/cheapestPerDay?outboundMonthOfDate=%s&currency=EUR", origin, destination, date);
        ResponseEntity<Root> response
                = restTemplate.getForEntity(fooResourceUrl, Root.class);

        var root = response.getBody();
        var res = root.getOutbound().getFares().stream().filter(fare -> fare.getPrice() != null).collect(Collectors.toList());
//
//        JsonPrettyPrinter jsonPrettyPrinter = new JsonPrettyPrinter();
//        String formattedJsonString = jsonPrettyPrinter.prettyPrintJsonUsingDefaultPrettyPrinter(response.getBody());
        return res;

    }

    @PostMapping
    @RequestMapping("/{origin}/{destination}/{date}")
    public List<Fare> post(@PathVariable String origin, @PathVariable String destination, @PathVariable String date) {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = String.format( "https://www.ryanair.com/api/farfnd/v4/oneWayFares/%s/%s/cheapestPerDay?outboundMonthOfDate=%s&currency=EUR", origin, destination, date);
        ResponseEntity<Root> response
                = restTemplate.getForEntity(fooResourceUrl, Root.class);

        var root = response.getBody();
        var res = root.getOutbound().getFares().stream().filter(fare -> fare.getPrice() != null).collect(Collectors.toList());

        var quote = new Quote();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
        LocalDate day = LocalDate.parse(date, formatter);
        quote.setDateYear(String.valueOf(day.getYear()));
        quote.setDateYear(String.valueOf(day.getYear()));

//
//        JsonPrettyPrinter jsonPrettyPrinter = new JsonPrettyPrinter();
//        String formattedJsonString = jsonPrettyPrinter.prettyPrintJsonUsingDefaultPrettyPrinter(response.getBody());
        return res;

    }

    @GetMapping
    @RequestMapping("/{origin}/{destination}/{date}/{price}")
    public List<Fare> gets(@PathVariable String origin, @PathVariable String destination, @PathVariable String date, @PathVariable long price) {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = String.format( "https://www.ryanair.com/api/farfnd/v4/oneWayFares/%s/%s/cheapestPerDay?outboundMonthOfDate=%s&currency=EUR", origin, destination, date);
        ResponseEntity<Root> response
                = restTemplate.getForEntity(fooResourceUrl, Root.class);

        var root = response.getBody();
        var res = root.getOutbound().getFares().stream().filter(fare -> fare.getPrice().getValue() != null).collect(Collectors.toList());
//
//        JsonPrettyPrinter jsonPrettyPrinter = new JsonPrettyPrinter();
//        String formattedJsonString = jsonPrettyPrinter.prettyPrintJsonUsingDefaultPrettyPrinter(response.getBody());
        return res;

    }
}
