package stream.arepresas.cryptotracker.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class DataUtils {
  public static String stringListToString(List<String> list) {
    return isNullOrEmptyList(list) ? "" : list.stream().collect(Collectors.joining(","));
  }

  public static boolean isNullOrEmptyList(List<?> value) {
    return value == null || value.isEmpty();
  }

  public static void logList(List<?> list) {
    list.forEach(value -> log.info(value.toString()));
  }
}
