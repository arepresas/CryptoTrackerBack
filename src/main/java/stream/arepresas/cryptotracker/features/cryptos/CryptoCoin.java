package stream.arepresas.cryptotracker.features.cryptos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "coinPrice")
@EqualsAndHashCode(exclude = "coinPrice")
@Table(name = "crypto_coin")
public class CryptoCoin implements Serializable {
  @Id
  @Column(name = "id", nullable = false)
  private Long id; // CoinMarketId

  @NotNull private String symbol;
  @NotNull private String name;

  private String category;
  private String description;
  private String slug;
  private String logo;
  private String subreddit;
  private String tags; // Tag comma separated
  private String tagNames; // TagNames comma separated
  private String tagGroups; // TagGroups comma separated
  private String tokenAddress;

  @JsonIgnore
  @OneToOne(
      mappedBy = "coinInfo",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private CryptoCoinPrice coinPrice;
}
