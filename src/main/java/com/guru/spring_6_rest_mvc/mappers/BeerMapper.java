package com.guru.spring_6_rest_mvc.mappers;

import com.guru.spring_6_rest_mvc.entities.Beer;
import com.guru.spring_6_rest_mvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDTOToBeer(BeerDTO dto);

    BeerDTO beerToBeerDTO(Beer beer);

}
