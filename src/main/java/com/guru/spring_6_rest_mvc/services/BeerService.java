package com.guru.spring_6_rest_mvc.services;

import com.guru.spring_6_rest_mvc.model.Beer;

import java.util.UUID;

public interface BeerService {
    Beer getBeerById(UUID id);
}

