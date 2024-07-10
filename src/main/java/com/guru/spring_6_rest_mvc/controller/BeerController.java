package com.guru.spring_6_rest_mvc.controller;

import com.guru.spring_6_rest_mvc.model.Beer;
import com.guru.spring_6_rest_mvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;


    @PutMapping(value = "{beerId}")
    public ResponseEntity updateById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        log.debug("BeerController -> updateById -> beerId: {} - beer {}", beerId, beer);
        beerService.updateById(beerId, beer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity handlePost(@RequestBody Beer beer) {
        log.debug("BeerController -> handlePost -> beer: {}", beer);
        Beer savedBeer = beerService.saveNewBeer(beer);
        //Manera manual de hacerlo
        //HttpHeaders headers = new HttpHeaders();
        //headers.add("Location", "/api/v1/beer/" + savedBeer.getId().toString());
        //return new ResponseEntity(headers, HttpStatus.CREATED);
        //Manera automatizada
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBeer.getId())
                .toUri();
        return ResponseEntity.created(location).body(null);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> listBeers() {
        log.debug("BeerController -> listBeers");
        return beerService.listBeers();
    }

    @RequestMapping(value = "{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("BeerController -> getBeerById -> beerId: {}", beerId);
        return beerService.getBeerById(beerId);
    }
}
