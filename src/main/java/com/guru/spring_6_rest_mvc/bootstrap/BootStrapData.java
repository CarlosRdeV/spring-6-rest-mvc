package com.guru.spring_6_rest_mvc.bootstrap;

import com.guru.spring_6_rest_mvc.entities.Beer;
import com.guru.spring_6_rest_mvc.entities.BeerCSVRecord;
import com.guru.spring_6_rest_mvc.entities.Customer;
import com.guru.spring_6_rest_mvc.model.BeerStyle;
import com.guru.spring_6_rest_mvc.repositories.BeerRepository;
import com.guru.spring_6_rest_mvc.repositories.CustomerRepository;
import com.guru.spring_6_rest_mvc.services.BeerCsvService;
import com.guru.spring_6_rest_mvc.services.BeerCsvServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootStrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;

    private final CustomerRepository customerRepository;

    private final BeerCsvService beerCsvService;

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        if (beerRepository.count() == 0) {
            loadBeerData();
        }

        if (customerRepository.count() == 0) {
            loadCustomerData();
        }

        if (beerRepository.count() < 10) {
            loadCsvData();
        }
    }

    private void loadCsvData() throws FileNotFoundException {
        log.debug("BootStrapData -> loadCsvData");
        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

        List<BeerCSVRecord> recs = beerCsvService.convertCSV(file);

        recs.forEach(beerCSVRecord -> {
            BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                case "American Pale Lager" -> BeerStyle.LAGER;
                case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                        BeerStyle.ALE;
                case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                case "American Porter" -> BeerStyle.PORTER;
                case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                case "English Pale Ale" -> BeerStyle.PALE_ALE;
                default -> BeerStyle.PILSNER;
            };

            beerRepository.save(Beer.builder()
                    .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(),50))
                    .beerStyle(beerStyle)
                    .price(BigDecimal.TEN)
                    .upc(beerCSVRecord.getRow().toString())
                    .quantityOnHand(beerCSVRecord.getCount())
                    .build());

        });
    }


    private void loadBeerData() {
        log.debug("BootStrapData -> loadBeerData");
        Beer beer1 = Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356333")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerRepository.saveAll(Arrays.asList(beer1, beer2, beer3));

    }

    private void loadCustomerData() {
        log.debug("BootStrapData -> loadCustomerData");

        Customer customer1 = Customer.builder()
                .customerName("Nissaxter")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .customerName("Xavy")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .customerName("Magnazero")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));

    }
}
