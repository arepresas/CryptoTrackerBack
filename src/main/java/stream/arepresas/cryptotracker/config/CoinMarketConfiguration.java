package stream.arepresas.cryptotracker.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.hateoas.config.HypermediaWebClientConfigurer;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@EnableHypermediaSupport(type = HypermediaType.HAL)
@Configuration
public class CoinMarketConfiguration {
  private static final int MAX_IN_MEMORY_SIZE_BYTES = 4 * 1024 * 1024;
  private static final int CONNECT_TIMEOUT_MILLIS = 5_000;
  private static final Duration RESPONSE_TIMEOUT = Duration.ofSeconds(10);
  private static final int READ_TIMEOUT_SECONDS = 10;
  private static final int WRITE_TIMEOUT_SECONDS = 10;

  @Bean
  public WebClient.Builder webClientBuilder() {
    ExchangeStrategies exchangeStrategies =
        ExchangeStrategies.builder()
            .codecs(
                configurer ->
                    configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAX_IN_MEMORY_SIZE_BYTES))
            .build();

    HttpClient httpClient =
        HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLIS)
            .responseTimeout(RESPONSE_TIMEOUT)
            .doOnConnected(
                conn ->
                    conn.addHandlerLast(
                            new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS))
                        .addHandlerLast(
                            new WriteTimeoutHandler(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)));

    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .exchangeStrategies(exchangeStrategies);
  }

  @Bean(value = "coinMarketWebClient")
  public WebClient webClient(
      WebClient.Builder webClientBuilder, HypermediaWebClientConfigurer configurer) {
    configurer.registerHypermediaTypes(webClientBuilder);
    return webClientBuilder.build();
  }
}
