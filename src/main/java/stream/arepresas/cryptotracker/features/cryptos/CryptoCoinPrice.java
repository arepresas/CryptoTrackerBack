package stream.arepresas.cryptotracker.features.cryptos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"coinPriceQuotes", "coinInfo"})
@EqualsAndHashCode(exclude = {"coinPriceQuotes", "coinInfo"})
@Table(name = "crypto_coin_price")
public class CryptoCoinPrice implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  private Long cmcRank;
  private Long numMarketPairs;
  private Double circulatingSupply;
  private Double totalSupply;
  private Double maxSupply;
  private Date dateAdded;
  private Long platformId;

  @JsonIgnore
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coin_info_id", referencedColumnName = "id")
  private CryptoCoin coinInfo;

  @JsonIgnore
  @OneToMany(
      mappedBy = "coinPrice",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private List<CryptoCoinQuote> coinPriceQuotes;
}
