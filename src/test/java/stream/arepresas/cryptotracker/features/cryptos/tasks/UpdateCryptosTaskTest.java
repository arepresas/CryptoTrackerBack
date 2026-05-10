package stream.arepresas.cryptotracker.features.cryptos.tasks;

import io.github.microcks.testcontainers.MicrocksContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.vault.VaultContainer;
import stream.arepresas.cryptotracker.features.cryptos.*;

import java.util.ArrayList;
import java.io.File;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
  "VAULT_URI=http://127.0.0.1:8200",
  "VAULT_TOKEN=test-token"
})
@ActiveProfiles("test")
@Testcontainers
class UpdateCryptosTaskTest {

  private static final DockerImageName MICROCKS_IMAGE =
    DockerImageName.parse("quay.io/microcks/microcks-uber:1.13.2");

  @Container
  @ServiceConnection
  static PostgreSQLContainer postgres =
    new PostgreSQLContainer("postgres:18-alpine")
      .withDatabaseName("cryptotracker-db")
      .withUsername("myuser")
      .withPassword("mysecretpassword");

  @Container
  static final VaultContainer<?> vault =
    new VaultContainer<>(DockerImageName.parse("hashicorp/vault:1.13.3"))
      .withVaultToken("test-token")
      .withSecretInVault("secret/crypto-tracker", "crypto-api.key=test-key")
      .withStartupTimeout(Duration.ofSeconds(60));

  static MicrocksContainer microcks =
    new MicrocksContainer(MICROCKS_IMAGE).withMainArtifacts("coinmarket-openapi.yaml");

  @DynamicPropertySource
  static void dynamicProperties(DynamicPropertyRegistry registry) {
    if (!microcks.isRunning()) {
      microcks.start();
      try {
        microcks.importAsMainArtifact(new File("target/test-classes/coinmarket-openapi.yaml"));
      } catch (Exception exception) {
        throw new IllegalStateException("Failed to import Microcks main artifact", exception);
      }
    }

    registry.add("VAULT_URI", vault::getHttpHostAddress);
    registry.add("VAULT_TOKEN", () -> "test-token");
    registry.add("coinMarket.api.url", () -> microcks.getRestMockEndpoint("CoinMarketAPI", "1.0.0"));
    registry.add("spring.cloud.vault.uri", vault::getHttpHostAddress);
    registry.add("spring.cloud.vault.token", () -> "test-token");
  }

  @Autowired
  private UpdateCryptosTask updateCryptosTask;
  @Autowired
  private CryptoCoinRepository cryptoCoinRepository;
  @Autowired
  private CryptoCoinPriceRepository cryptoCoinPriceRepository;
  @Autowired
  private CryptoCoinQuoteRepository cryptoCoinQuoteRepository;

  @BeforeEach
  void setUp() {
    cryptoCoinQuoteRepository.deleteAll();
    cryptoCoinPriceRepository.deleteAll();
    cryptoCoinRepository.deleteAll();

    CryptoCoin existingCoin =
      CryptoCoin.builder()
        .id(3L)
        .symbol("DOGE")
        .name("Dogecoin")
        .category("coin")
        .slug("dogecoin")
        .build();

    CryptoCoin savedCoin = cryptoCoinRepository.save(existingCoin);

    CryptoCoinPrice existingPrice =
      CryptoCoinPrice.builder()
        .coinInfo(savedCoin)
        .cmcRank(10L)
        .numMarketPairs(300L)
        .circulatingSupply(140000000000.0)
        .totalSupply(140000000000.0)
        .maxSupply(null)
        .coinPriceQuotes(new ArrayList<>())
        .build();

    cryptoCoinPriceRepository.save(existingPrice);
  }

  @AfterAll
  static void tearDown() {
    if (microcks.isRunning()) {
      microcks.stop();
    }
  }

  @Test
  void run_shouldSaveNewCoinsAndQuotesForExistingCoins() {
    updateCryptosTask.run();

    assertThat(cryptoCoinRepository.findCryptoCoinIds()).contains(2L, 3L).doesNotContain(1L);
    assertThat(cryptoCoinRepository.findById(2L)).isPresent();
    assertThat(cryptoCoinRepository.findById(2L).orElseThrow().getSymbol()).isEqualTo("ETH");

    assertThat(cryptoCoinPriceRepository.count()).isEqualTo(2);
    assertThat(cryptoCoinQuoteRepository.count()).isEqualTo(3);
    assertThat(cryptoCoinQuoteRepository.findAll().stream().map(CryptoCoinQuote::getPrice).toList())
      .contains(3200.0, 0.22);
  }
}
