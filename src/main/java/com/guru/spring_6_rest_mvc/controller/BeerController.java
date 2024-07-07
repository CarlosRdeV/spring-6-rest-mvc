package com.guru.spring_6_rest_mvc.controller;

import com.guru.spring_6_rest_mvc.model.Beer;
import com.guru.spring_6_rest_mvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Controller
public class BeerController {
    private final BeerService beerService;

    public Beer getBeerById(UUID id){
        log.debug("BeerController -> getBeerById -> id: {}", id);
        return beerService.getBeerById(id);
    }
}
