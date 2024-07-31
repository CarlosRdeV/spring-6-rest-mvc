package com.guru.spring_6_rest_mvc.services;

import com.guru.spring_6_rest_mvc.mappers.BeerMapper;
import com.guru.spring_6_rest_mvc.model.BeerDTO;
import com.guru.spring_6_rest_mvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;

    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeers() {
        log.debug("BeerServiceJPA -> listBeers");
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beerToBeerDTO)
                .collect(Collectors.toList()
                );
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID beerId) {
        log.debug("BeerServiceJPA -> getBeerById -> beerId: {}",beerId);
        return Optional.ofNullable(
                beerMapper.beerToBeerDTO(beerRepository
                        .findById(beerId)
                        .orElse(null)
                )
        );
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        log.debug("BeerServiceJPA -> saveNewBeer -> beer: {}", beer);
        return beerMapper.beerToBeerDTO(
                beerRepository.save(
                        beerMapper.beerDTOToBeer(beer)
                )
        );
    }

    @Override
    public Optional<BeerDTO> updateById(UUID beerId, BeerDTO beer) {
        log.debug("BeerServiceJPA -> updateById -> beerId: {} -> beer: {}", beerId, beer);
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(
                foundBeer -> {
                    foundBeer.setBeerName(beer.getBeerName());
                    foundBeer.setBeerStyle(beer.getBeerStyle());
                    foundBeer.setUpc(beer.getUpc());
                    foundBeer.setPrice(beer.getPrice());
                    atomicReference.set(
                            Optional.of(
                                    beerMapper.beerToBeerDTO(beerRepository.save(foundBeer))));
                }, () -> {
                    atomicReference.set(Optional.empty());
                });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID beerId) {
        log.debug("BeerServiceJPA -> deleteById -> beerId: {}", beerId);
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

    @Override
    public void patchBeerById(UUID beerId, BeerDTO beer) {

    }
}
