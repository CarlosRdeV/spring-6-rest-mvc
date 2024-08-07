package com.guru.spring_6_rest_mvc.services;

import com.guru.spring_6_rest_mvc.entities.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCsvService {

    List<BeerCSVRecord> convertCSV(File file);
}
