package stream.arepresas.cryptotracker.features.cryptos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import stream.arepresas.cryptotracker.shared.criteria.PaginationCriteria;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CryptoCoinCriteria extends PaginationCriteria {
  private List<Long> ids;
  private String symbol;
  private String name;
  private String category;
  private String description;
  private String slug;
  private String logo;
  private String subreddit;
  private String tags; // Tag comma separated
  private String tagNames; // TagNames comma separated
  private String tagGroups; // TagGroups comma separated
  private String tokenAddress;

  public PageRequest generatePageRequest() {
    var initSort = Sort.by(Sort.Order.asc("id"));
    return super.generatePageRequest(initSort);
  }
}
