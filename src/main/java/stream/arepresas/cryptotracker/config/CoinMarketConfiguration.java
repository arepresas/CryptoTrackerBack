package stream.arepresas.cryptotracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.hateoas.config.HypermediaWebClientConfigurer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@EnableHypermediaSupport(type = HypermediaType.HAL)
@Configuration
public class CoinMarketConfiguration {
  private static final int MAX_IN_MEMORY_SIZE_BYTES = 4 * 1024 * 1024;

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

    return WebClient.builder().exchangeStrategies(exchangeStrategies);
  }

  @Bean(value = "coinMarketWebClient")
  public WebClient webClient(
      WebClient.Builder webClientBuilder, HypermediaWebClientConfigurer configurer) {
    configurer.registerHypermediaTypes(webClientBuilder);
    return webClientBuilder.build();
  }
}
