package stream.arepresas.cryptotracker.features.cryptos;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stream.arepresas.cryptotracker.utils.DataUtils;

import java.util.List;

import static stream.arepresas.cryptotracker.utils.DataUtils.isNullOrEmpty;
import static stream.arepresas.cryptotracker.utils.DataUtils.listToString;
import static stream.arepresas.cryptotracker.utils.DataUtils.logCriteria;

@RequiredArgsConstructor
@Service
@Slf4j
public class CryptoCoinService {
  private final CryptoCoinRepository cryptoCoinRepository;
  private final CryptoCoinPriceRepository cryptoCoinPriceRepository;
  private final CryptoCoinQuoteRepository cryptoCoinQuoteRepository;

  // GET

  @Transactional(readOnly = true)
  public List<Long> getCryptoCoinIds() {
    log.info("CryptoCoinService - getCryptoCoinIds");
    return cryptoCoinRepository.findCryptoCoinIds();
  }

  @Transactional(readOnly = true)
  public CryptoCoin getCryptoCoin(final Long cryptoCoinId) {
    log.info("CryptoCoinService - getCryptoCoin {}", cryptoCoinId);
    return cryptoCoinId != null ? this.cryptoCoinRepository.getReferenceById(cryptoCoinId) : null;
  }

  public Page<CryptoCoin> searchCryptoCoins(final CryptoCoinCriteria cryptoCoinCriteria) {
    log.info("CryptoCoinService - searchCryptos");
    logCriteria(cryptoCoinCriteria);
    return cryptoCoinRepository.findAll(
        getSearchCryptoCoinPredicate(cryptoCoinCriteria), cryptoCoinCriteria.generatePageRequest());
  }

  private Predicate getSearchCryptoCoinPredicate(
      @NonNull final CryptoCoinCriteria cryptoCoinCriteria) {
    final QCryptoCoin qCryptoCoinPrice = QCryptoCoin.cryptoCoin;
    final BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(qCryptoCoinPrice.id.isNotNull());

    if (!isNullOrEmpty(cryptoCoinCriteria.getIds()))
      predicate.and(qCryptoCoinPrice.id.in(cryptoCoinCriteria.getIds()));

    if (!isNullOrEmpty(cryptoCoinCriteria.getSymbol()))
      predicate.and(qCryptoCoinPrice.symbol.likeIgnoreCase(cryptoCoinCriteria.getSymbol()));

    if (!isNullOrEmpty(cryptoCoinCriteria.getName()))
      predicate.and(qCryptoCoinPrice.name.likeIgnoreCase(cryptoCoinCriteria.getName()));

    if (!isNullOrEmpty(cryptoCoinCriteria.getCategory()))
      predicate.and(qCryptoCoinPrice.category.likeIgnoreCase(cryptoCoinCriteria.getCategory()));

    if (!isNullOrEmpty(cryptoCoinCriteria.getSlug()))
      predicate.and(qCryptoCoinPrice.slug.likeIgnoreCase(cryptoCoinCriteria.getSlug()));

    if (!isNullOrEmpty(cryptoCoinCriteria.getLogo()))
      predicate.and(qCryptoCoinPrice.logo.likeIgnoreCase(cryptoCoinCriteria.getLogo()));

    if (!isNullOrEmpty(cryptoCoinCriteria.getSubreddit()))
      predicate.and(qCryptoCoinPrice.subreddit.likeIgnoreCase(cryptoCoinCriteria.getSubreddit()));

    if (!isNullOrEmpty(cryptoCoinCriteria.getTags())) {
      List<String> tags = DataUtils.stringToList(cryptoCoinCriteria.getTags());
      predicate.and(qCryptoCoinPrice.tags.in(tags));
    }

    if (!isNullOrEmpty(cryptoCoinCriteria.getTagNames())) {
      List<String> tagNames = DataUtils.stringToList(cryptoCoinCriteria.getTagNames());
      predicate.and(qCryptoCoinPrice.tagNames.in(tagNames));
    }

    if (!isNullOrEmpty(cryptoCoinCriteria.getTagGroups())) {
      List<String> tagGroups = DataUtils.stringToList(cryptoCoinCriteria.getTagGroups());
      predicate.and(qCryptoCoinPrice.tagGroups.in(tagGroups));
    }

    if (!isNullOrEmpty(cryptoCoinCriteria.getTokenAddress()))
      predicate.and(
          qCryptoCoinPrice.tokenAddress.likeIgnoreCase(cryptoCoinCriteria.getTokenAddress()));

    return predicate;
  }

  @Transactional(readOnly = true)
  public CryptoCoinPrice getCryptoCoinPrice(final Long cryptoCoinPriceId) {
    log.info("CryptoCoinService - getCryptoCoinPrice {}", cryptoCoinPriceId);
    return cryptoCoinPriceId != null
        ? this.cryptoCoinPriceRepository.getReferenceById(cryptoCoinPriceId)
        : null;
  }

  public Page<CryptoCoinPrice> searchCryptoCoinPrices(
      final CryptoCoinPriceCriteria cryptoCoinPriceCriteria) {
    log.info("CryptoCoinService - searchCryptoPrices");
    logCriteria(cryptoCoinPriceCriteria);
    return cryptoCoinPriceRepository.findAll(
        getSearchCryptoCoinPricePredicate(cryptoCoinPriceCriteria),
        cryptoCoinPriceCriteria.generatePageRequest());
  }

  private Predicate getSearchCryptoCoinPricePredicate(
      @NonNull CryptoCoinPriceCriteria cryptoCoinPriceCriteria) {
    final QCryptoCoinPrice qCryptoCoinPrice = QCryptoCoinPrice.cryptoCoinPrice;
    final BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(qCryptoCoinPrice.id.isNotNull());

    if (!isNullOrEmpty(cryptoCoinPriceCriteria.getIds()))
      predicate.and(qCryptoCoinPrice.id.in(cryptoCoinPriceCriteria.getIds()));

    if (cryptoCoinPriceCriteria.getCmcRank() != null)
      predicate.and(qCryptoCoinPrice.cmcRank.eq(cryptoCoinPriceCriteria.getCmcRank()));

    if (cryptoCoinPriceCriteria.getNumMarketPairs() != null)
      predicate.and(
          qCryptoCoinPrice.numMarketPairs.eq(cryptoCoinPriceCriteria.getNumMarketPairs()));

    if (!isNullOrEmpty(cryptoCoinPriceCriteria.getCryptoCoinIds()))
      predicate.and(qCryptoCoinPrice.coinInfo.id.in(cryptoCoinPriceCriteria.getCryptoCoinIds()));

    if (!isNullOrEmpty(cryptoCoinPriceCriteria.getCryptoCoinSymbols()))
      predicate.and(
          qCryptoCoinPrice.coinInfo.symbol.in(cryptoCoinPriceCriteria.getCryptoCoinSymbols()));

    return predicate;
  }

  public CryptoCoinQuote getCryptoCoinQuote(Long cryptoCoinQuoteId) {
    log.info("CryptoCoinService - getCryptoCoinQuote {}", cryptoCoinQuoteId);
    return cryptoCoinQuoteId != null
        ? this.cryptoCoinQuoteRepository.getReferenceById(cryptoCoinQuoteId)
        : null;
  }

  public Page<CryptoCoinQuote> searchCryptoCoinQuotes(
      CryptoCoinQuoteCriteria cryptoCoinQuoteCriteria) {
    log.info("CryptoCoinService - searchCryptoQuotes");
    logCriteria(cryptoCoinQuoteCriteria);
    return cryptoCoinQuoteRepository.findAll(
        getSearchCryptoCoinQuotePredicate(cryptoCoinQuoteCriteria),
        cryptoCoinQuoteCriteria.generatePageRequest());
  }

  private Predicate getSearchCryptoCoinQuotePredicate(
      @NonNull CryptoCoinQuoteCriteria cryptoCoinQuoteCriteria) {
    final QCryptoCoinQuote qCryptoCoinQuote = QCryptoCoinQuote.cryptoCoinQuote;
    final BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(qCryptoCoinQuote.id.isNotNull());

    if (!isNullOrEmpty(cryptoCoinQuoteCriteria.getIds()))
      predicate.and(qCryptoCoinQuote.id.in(cryptoCoinQuoteCriteria.getIds()));

    if (!isNullOrEmpty(cryptoCoinQuoteCriteria.getCurrency()))
      predicate.and(
          qCryptoCoinQuote.currency.stringValue().eq(cryptoCoinQuoteCriteria.getCurrency()));

    if (!isNullOrEmpty(cryptoCoinQuoteCriteria.getCryptoPriceIds()))
      predicate.and(qCryptoCoinQuote.coinPrice.id.in(cryptoCoinQuoteCriteria.getCryptoPriceIds()));

    if (cryptoCoinQuoteCriteria.getLastUpdatedBefore() != null)
      predicate.and(
          qCryptoCoinQuote.lastUpdated.before(cryptoCoinQuoteCriteria.getLastUpdatedBefore()));

    if (cryptoCoinQuoteCriteria.getLastUpdatedAfter() != null)
      predicate.and(
          qCryptoCoinQuote.lastUpdated.after(cryptoCoinQuoteCriteria.getLastUpdatedAfter()));

    return predicate;
  }

  // SAVE

  public List<CryptoCoin> saveCryptoCoins(@NonNull List<CryptoCoin> cryptoCoins) {
    log.info(
        "CryptoCoinService - saveCryptoCoins {}",
        listToString(cryptoCoins.stream().map(CryptoCoin::getId).toList()));

    List<CryptoCoin> savedCryptoCoins = cryptoCoinRepository.saveAll(cryptoCoins);

    if (savedCryptoCoins.isEmpty()) {
      log.info("No new cryptos to save");
    } else {
      log.info(
          "Saved {} cryptoCoins with Ids {}",
          savedCryptoCoins.size(),
          listToString(savedCryptoCoins.stream().map(CryptoCoin::getId).toList()));
    }

    return savedCryptoCoins;
  }

  public CryptoCoinPrice saveCryptoCoinPrice(@NonNull CryptoCoinPrice cryptoCoinPrice) {
    log.info("CryptoCoinService - saveCryptoCoinPrice {}", cryptoCoinPrice);
    return cryptoCoinPriceRepository.save(cryptoCoinPrice);
  }

  public List<CryptoCoinPrice> saveCryptoCoinPrices(
      @NonNull List<CryptoCoinPrice> cryptoCoinPrices) {
    log.info(
        "CryptoCoinService - saveCryptoCoinPrices {}",
        listToString(cryptoCoinPrices.stream().map(CryptoCoinPrice::getId).toList()));

    List<CryptoCoinPrice> savedCryptoCoinPrices =
        cryptoCoinPriceRepository.saveAll(cryptoCoinPrices);

    if (savedCryptoCoinPrices.isEmpty()) {
      log.info("No new cryptoCoinPrices to save");
    } else {
      log.info(
          "Saved {} cryptoCoinPrices with Ids {}",
          savedCryptoCoinPrices.size(),
          listToString(
              savedCryptoCoinPrices.stream()
                  .map(cryptoCoin -> cryptoCoin.getCoinInfo().getId())
                  .toList()));
    }

    return savedCryptoCoinPrices;
  }

  public List<CryptoCoinQuote> saveCryptoCoinPriceQuotes(
      @NonNull List<CryptoCoinQuote> cryptoCoinQuotes) {
    log.info(
        "CryptoCoinService - saveCryptoCoinPriceQuotes for cryptoCoins {}",
        cryptoCoinQuotes.stream()
            .map(
                cryptoCoinPriceQuote ->
                    cryptoCoinPriceQuote.getCoinPrice().getCoinInfo().getSymbol())
            .toList());

    List<CryptoCoinQuote> savedCryptoCoinQuotes =
        cryptoCoinQuoteRepository.saveAll(cryptoCoinQuotes);

    if (savedCryptoCoinQuotes.isEmpty()) {
      log.info("No new cryptoCoinPriceQuotes to save");
    } else {
      log.info(
          "Saved {} cryptoCoinPriceQuotes with Ids {}",
          savedCryptoCoinQuotes.size(),
          savedCryptoCoinQuotes.stream()
              .map(cryptoCoin -> cryptoCoin.getCoinPrice().getCoinInfo().getId())
              .toList());
    }

    return savedCryptoCoinQuotes;
  }
}
