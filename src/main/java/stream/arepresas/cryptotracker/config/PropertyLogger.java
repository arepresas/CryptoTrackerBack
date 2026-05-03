package stream.arepresas.cryptotracker.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.StreamSupport;

@Slf4j
@Component
@Profile("local")
public class PropertyLogger {

  private static final List<String> SENSITIVE_PROPERTY_TOKENS =
      List.of(
          "password",
          "secret",
          "token",
          "credential",
          "api.key",
          "api-key",
          "access-key",
          "private-key",
          "secret-key",
          "authorization");

  @EventListener
  public void handleContextRefresh(ContextRefreshedEvent event) {
    Environment env = event.getApplicationContext().getEnvironment();
    log.info("====== Environment and configuration ======");
    log.info("Active profiles: {}", Arrays.toString(env.getActiveProfiles()));
    MutablePropertySources sources = ((AbstractEnvironment) env).getPropertySources();
    StreamSupport.stream(sources.spliterator(), false)
        .filter(EnumerablePropertySource.class::isInstance)
        .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
        .flatMap(Arrays::stream)
        .distinct()
        .sorted()
        .forEach(prop -> log.info("{}: {}", prop, sanitizeValue(prop, env.getProperty(prop))));
    log.info("===========================================");
  }

  private static String sanitizeValue(String propertyName, String propertyValue) {
    if (propertyValue == null) {
      return null;
    }

    if (!isSensitiveProperty(propertyName)) {
      return propertyValue;
    }

    if (propertyValue.length() <= 4) {
      return "****";
    }

    return propertyValue.substring(0, 2) + "****" + propertyValue.substring(propertyValue.length() - 2);
  }

  private static boolean isSensitiveProperty(String propertyName) {
    String normalizedPropertyName = propertyName.toLowerCase(Locale.ROOT);
    return SENSITIVE_PROPERTY_TOKENS.stream().anyMatch(normalizedPropertyName::contains);
  }
}
