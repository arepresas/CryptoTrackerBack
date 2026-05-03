package stream.arepresas.cryptotracker.external.coinmarket;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import stream.arepresas.cryptotracker.external.coinmarket.dto.CoinMarketApiResponse;
import stream.arepresas.cryptotracker.external.coinmarket.dto.CoinMarketInfoApiResponse;
import stream.arepresas.cryptotracker.external.coinmarket.dto.CoinMarketLastListingApiResponse;
import stream.arepresas.cryptotracker.external.coinmarket.dto.CoinMarketLastQuoteApiResponse;
import stream.arepresas.cryptotracker.features.cryptos.Currency;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static stream.arepresas.cryptotracker.utils.ApiUtils.getResponse;
import static stream.arepresas.cryptotracker.utils.ApiUtils.logQuery;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinMarketClient {

  private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);
  public static final String START = "start";
  public static final String LIMIT = "limit";
  public static final String CONVERT = "convert";
  public static final String ID = "id";
  private final WebClient coinMarketWebClient;

  @Value("${coinMarket.api.key}")
  private String apiKey;

  @Value("${coinMarket.api.url}")
  private String mainUrl;

  public CoinMarketApiResponse getCryptoInfo(List<Long> cryptoIds) {
    log.info("CoinMarketClient - getCryptoInfo");

    String url = mainUrl.concat("/v2/cryptocurrency/info");

    String urlTemplate =
        UriComponentsBuilder.fromUriString(url).queryParam(ID, "{id}").encode().toUriString();

    Map<String, ?> params =
        Map.of(ID, cryptoIds.stream().map(String::valueOf).collect(Collectors.joining(",")));

    logQuery(urlTemplate, params);

    ResponseEntity<CoinMarketInfoApiResponse> coinMarketApiResponse = coinMarketWebClient
            .method(HttpMethod.GET)
            .uri(urlTemplate, params)
            .headers(httpHeaders -> httpHeaders.addAll(createHttpHeaders()))
            .retrieve()
            .toEntity(CoinMarketInfoApiResponse.class)
            .timeout(REQUEST_TIMEOUT)
            .block();

    return getResponse(coinMarketApiResponse);
  }

  public CoinMarketApiResponse getCryptoLastPrices(
      Integer start, Integer limit, Currency currency) {
    log.info("CoinMarketClient - getCryptoLastPrices");

    String url = mainUrl.concat("/v1/cryptocurrency/listings/latest");

    String urlTemplate =
        UriComponentsBuilder.fromUriString(url)
            .queryParam(START, "{start}")
            .queryParam(LIMIT, "{limit}")
            .queryParam(CONVERT, "{convert}")
            .encode()
            .toUriString();

    Map<String, ?> params = Map.of(START, start, LIMIT, limit, CONVERT, currency);

    logQuery(urlTemplate, params);

    ResponseEntity<CoinMarketLastListingApiResponse> coinMarketApiResponse = coinMarketWebClient
            .method(HttpMethod.GET)
            .uri(urlTemplate, params)
            .headers(httpHeaders -> httpHeaders.addAll(createHttpHeaders()))
            .retrieve()
            .toEntity(CoinMarketLastListingApiResponse.class)
            .timeout(REQUEST_TIMEOUT)
            .block();

    return getResponse(coinMarketApiResponse);
  }

  public CoinMarketApiResponse getCryptoPrices(
      @NonNull List<Long> cryptoIds, @NonNull Currency currency) {
    log.info("CoinMarketClient - getCryptoPrices");

    String url = mainUrl.concat("/v2/cryptocurrency/quotes/latest");

    String urlTemplate =
        UriComponentsBuilder.fromUriString(url)
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

    ResponseEntity<CoinMarketLastQuoteApiResponse> coinMarketApiResponse = coinMarketWebClient
            .method(HttpMethod.GET)
            .uri(urlTemplate, params)
            .headers(httpHeaders -> httpHeaders.addAll(createHttpHeaders()))
            .retrieve()
            .toEntity(CoinMarketLastQuoteApiResponse.class)
            .timeout(REQUEST_TIMEOUT)
            .block();

    return getResponse(coinMarketApiResponse);
  }

  //  TODO
  // Price Conversion v2

  private HttpHeaders createHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.ACCEPT, "application/json");
    headers.add("X-CMC_PRO_API_KEY", apiKey);
    return headers;
  }
}
