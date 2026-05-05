package stream.arepresas.cryptotracker.features.cryptos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import stream.arepresas.cryptotracker.shared.criteria.PaginationCriteria;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CryptoCoinQuoteCriteria extends PaginationCriteria {

  private List<Long> ids;
  private String currency;
  private Date lastUpdatedBefore;
  private Date lastUpdatedAfter;
  private List<Long> cryptoPriceIds;

  public PageRequest generatePageRequest() {
    var initSort = Sort.by(Sort.Order.asc("id"));
    return super.generatePageRequest(initSort);
  }
}
