package com.guru.spring_6_rest_mvc.controller;

import com.guru.spring_6_rest_mvc.model.BeerDTO;
import com.guru.spring_6_rest_mvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {

    public static final String BEER_URI = "/api/v1/beer";
    public static final String BEER_BY_ID_URI = BEER_URI + "/{beerId}";
    private final BeerService beerService;


    @GetMapping(BEER_BY_ID_URI)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("BeerController -> getBeerById -> beerId: {}", beerId);
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @GetMapping(BEER_URI)
    public List<BeerDTO> listBeers() {
        log.debug("BeerController -> listBeers");
        return beerService.listBeers();
    }

    @PostMapping(BEER_URI)
    public ResponseEntity handlePost(@RequestBody BeerDTO beer) {
        log.debug("BeerController -> handlePost -> beer: {}", beer);
        BeerDTO savedBeer = beerService.saveNewBeer(beer);
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

    @PutMapping(BEER_BY_ID_URI)
    public ResponseEntity updateById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beer) {
        log.debug("BeerController -> updateById -> beerId: {} - beer {}", beerId, beer);
        if (beerService.updateById(beerId, beer).isEmpty()) {
            throw new NotFoundException();
        }
        // return new ResponseEntity(HttpStatus.NO_CONTENT); es lo mismo
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(BEER_BY_ID_URI)
    public ResponseEntity deleteById(@PathVariable("beerId") UUID beerId) {
        log.debug("BeerController -> deleteById -> beerId: {}", beerId);
        if (!beerService.deleteById(beerId)) {
            throw new NotFoundException();
        }
        // return new ResponseEntity(HttpStatus.NO_CONTENT); es lo mismo
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(BEER_BY_ID_URI)
    public ResponseEntity patchBeerById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beer) {
        log.debug("BeerController -> updateBeerPatchById -> beerId: {} - beer {}", beerId, beer);
        if (beerService.patchBeerById(beerId, beer).isEmpty()){
            throw new NotFoundException();
        }
        return ResponseEntity.noContent().build();
    }


}
