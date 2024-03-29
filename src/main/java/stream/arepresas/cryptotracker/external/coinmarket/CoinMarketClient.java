package stream.arepresas.cryptotracker.external.coinmarket;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import stream.arepresas.cryptotracker.external.coinmarket.dto.CoinMarketApiResponse;
import stream.arepresas.cryptotracker.external.coinmarket.dto.CoinMarketInfoApiResponse;
import stream.arepresas.cryptotracker.external.coinmarket.dto.CoinMarketLastListingApiResponse;
import stream.arepresas.cryptotracker.external.coinmarket.dto.CoinMarketLastQuoteApiResponse;
import stream.arepresas.cryptotracker.features.cryptos.Currency;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static stream.arepresas.cryptotracker.utils.ApiUtils.getResponse;
import static stream.arepresas.cryptotracker.utils.ApiUtils.logQuery;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinMarketClient {

  public static final String START = "start";
  public static final String LIMIT = "limit";
  public static final String CONVERT = "convert";
  public static final String ID = "id";
  private final RestTemplate coinMarketRestTemplate;

  @Value("${coinMarket.api.key}")
  private String apiKey;

  @Value("${coinMarket.api.url}")
  private String mainUrl;

  public CoinMarketApiResponse getCryptoInfo(List<Long> cryptoIds) {
    log.info("CoinMarketClient - getCryptoInfo");

    String url = mainUrl.concat("/v2/cryptocurrency/info");

    String urlTemplate =
        UriComponentsBuilder.fromHttpUrl(url).queryParam(ID, "{id}").encode().toUriString();

    Map<String, ?> params =
        Map.of(ID, cryptoIds.stream().map(String::valueOf).collect(Collectors.joining(",")));

    logQuery(urlTemplate, params);

    return getResponse(
        coinMarketRestTemplate.exchange(
            urlTemplate,
            HttpMethod.GET,
            createHttpEntity(),
            CoinMarketInfoApiResponse.class,
            params));
  }

  public CoinMarketApiResponse getCryptoLastPrices(
      Integer start, Integer limit, Currency currency) {
    log.info("CoinMarketClient - getCryptoLastPrices");

    String url = mainUrl.concat("/v1/cryptocurrency/listings/latest");

    String urlTemplate =
        UriComponentsBuilder.fromHttpUrl(url)
            .queryParam(START, "{start}")
            .queryParam(LIMIT, "{limit}")
            .queryParam(CONVERT, "{convert}")
            .encode()
            .toUriString();

    Map<String, ?> params = Map.of(START, start, LIMIT, limit, CONVERT, currency);

    logQuery(urlTemplate, params);

    return getResponse(
        coinMarketRestTemplate.exchange(
            urlTemplate,
            HttpMethod.GET,
            createHttpEntity(),
            CoinMarketLastListingApiResponse.class,
            params));
  }

  public CoinMarketApiResponse getCryptoPrices(
      @NonNull List<Long> cryptoIds, @NonNull Currency currency) {
    log.info("CoinMarketClient - getCryptoLastPrices");

    String url = mainUrl.concat("/v2/cryptocurrency/quotes/latest");

    String urlTemplate =
        UriComponentsBuilder.fromHttpUrl(url)
            .queryParam(ID, "{id}")
            .queryParam(CONVERT, "{convert}")
            .encode()
            .toUriString();

    Map<String, ?> params =
        Map.of(
            ID,
            cryptoIds.stream().map(String::valueOf).collect(Collectors.joining(",")),
            CONVERT,
            currency);

    logQuery(urlTemplate, params);

    return getResponse(
        coinMarketRestTemplate.exchange(
            urlTemplate,
            HttpMethod.GET,
            createHttpEntity(),
            CoinMarketLastQuoteApiResponse.class,
            params));
  }

  //  TODO
  // Price Conversion v2

  private HttpEntity<?> createHttpEntity() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.ACCEPT, "application/json");
    httpHeaders.add("X-CMC_PRO_API_KEY", apiKey);

    return new HttpEntity<>(httpHeaders);
  }
}
