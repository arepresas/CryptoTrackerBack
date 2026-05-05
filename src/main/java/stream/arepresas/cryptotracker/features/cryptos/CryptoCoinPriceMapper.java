package stream.arepresas.cryptotracker.features.cryptos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CryptoCoinPriceMapper {

  @Mapping(target = "coinInfo", ignore = true)
  @Mapping(target = "coinPriceQuotes", ignore = true)
  CryptoCoinPrice toEntity(CryptoCoinPriceDto dto);

  List<CryptoCoinPrice> toEntities(List<CryptoCoinPriceDto> dtos);

  CryptoCoinPriceDto toDto(CryptoCoinPrice cryptoCoinPrice);

  List<CryptoCoinPriceDto> toDtos(List<CryptoCoinPrice> cryptoCoinPrices);
}
