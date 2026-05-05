package stream.arepresas.cryptotracker.features.cryptos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptoCoinRepository extends JpaRepository<CryptoCoin, Long>, QuerydslPredicateExecutor<CryptoCoin> {

  @Query("select c.id from CryptoCoin c")
  List<Long> findCryptoCoinIds();
}
