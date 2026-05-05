package stream.arepresas.cryptotracker.features.cryptos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CryptoCoinQuoteMapper {

  @Mapping(target = "coinPrice", ignore = true)
  CryptoCoinQuote toEntity(CryptoCoinQuoteDto dto);

  List<CryptoCoinQuote> toEntities(List<CryptoCoinQuoteDto> dtos);

  CryptoCoinQuoteDto toDto(CryptoCoinQuote cryptoCoinQuote);

  List<CryptoCoinQuoteDto> toDtos(List<CryptoCoinQuote> cryptoCoinQuotes);
}
