package com.guru.spring_6_rest_mvc.services;

import com.guru.spring_6_rest_mvc.entities.Beer;
import com.guru.spring_6_rest_mvc.mappers.BeerMapper;
import com.guru.spring_6_rest_mvc.model.BeerDTO;
import com.guru.spring_6_rest_mvc.model.BeerStyle;
import com.guru.spring_6_rest_mvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
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

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGO_SIZE = 25;

    @Override
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
                                   Integer pageNumber, Integer pageSize) {
        log.debug("BeerServiceJPA -> listBeers -> beerName: {} -> beerStyle -> {} -> showInventory -> {} -> pageNumber -> {} -> pageSize -> {}",
                beerName, beerStyle, showInventory, pageNumber, pageSize);

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        List<Beer> beerList;

        if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerList = listBeersByNameAndBeerStyle(beerName, beerStyle);
        } else if (StringUtils.hasText(beerName) && beerStyle == null) {
            beerList = listBeersByName(beerName);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerList = listBeersByBeerStyle(beerStyle);
        } else {
            beerList = beerRepository.findAll();
        }

        if (showInventory != null && !showInventory) {
            beerList.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerList.stream()
                .map(beerMapper::beerToBeerDTO)
                .collect(Collectors.toList()
                );
    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGO_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        return PageRequest.of(queryPageNumber, queryPageSize);


    }

    public List<Beer> listBeersByNameAndBeerStyle(String beerName, BeerStyle beerStyle) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle);
    }

    public List<Beer> listBeersByBeerStyle(BeerStyle beerStyle) {
        return beerRepository.findAllByBeerStyle(beerStyle);
    }

    public List<Beer> listBeersByName(String beerName) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%");
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID beerId) {
        log.debug("BeerServiceJPA -> getBeerById -> beerId: {}", beerId);
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
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
        log.debug("BeerServiceJPA -> patchBeerById -> beerId: {} -> beer: {}", beerId, beer);
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(
                foundBeer -> {
                    if (StringUtils.hasText(beer.getBeerName())) {
                        foundBeer.setBeerName(beer.getBeerName());
                    }
                    if (beer.getBeerStyle() != null) {
                        foundBeer.setBeerStyle(beer.getBeerStyle());
                    }
                    if (StringUtils.hasText(beer.getUpc())) {
                        foundBeer.setUpc(beer.getUpc());
                    }
                    if (beer.getQuantityOnHand() != null) {
                        foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
                    }
                    if (beer.getPrice() != null) {
                        foundBeer.setPrice(beer.getPrice());
                    }

                    foundBeer.setUpdateDate(LocalDateTime.now());

                    atomicReference.set(
                            Optional.of(
                                    beerMapper.beerToBeerDTO(beerRepository.save(foundBeer))));
                }, () -> {
                    atomicReference.set(Optional.empty());
                });

        return atomicReference.get();
    }
}
