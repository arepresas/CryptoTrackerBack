package stream.arepresas.cryptotracker.features.cryptos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoCoinQuoteRepository extends JpaRepository<CryptoCoinQuote, Long>, QuerydslPredicateExecutor<CryptoCoinQuote> {
}
