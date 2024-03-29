package stream.arepresas.cryptotracker.features.cryptos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoCoinPriceRepository
    extends JpaRepository<CryptoCoinPrice, Long>, QuerydslPredicateExecutor<CryptoCoinPrice> {}
