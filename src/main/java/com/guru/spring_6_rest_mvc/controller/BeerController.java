package com.guru.spring_6_rest_mvc.controller;

import com.guru.spring_6_rest_mvc.services.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class BeerController {
    private final BeerService beerService;
}
