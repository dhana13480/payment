package org.finra.rmcs.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.finra.rmcs.constant.Constants;
import org.finra.rmcs.dto.AciTokenResponse;
import org.finra.rmcs.exception.InternalValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class GetAciTokenServiceImpl implements GetAciTokenService {

  private final String aciTokenUrl;
  private final String authKey;
  private final String clientId;
  private final String clientSecret;
  private final String grantType;
  private final RestTemplate restTemplate;

  @Autowired
  public GetAciTokenServiceImpl(
      @Value("${aci.client.tokenUrl}") String aciTokenUrl,
      @Value("${aci.client.x-auth-key}") String authKey,
      @Value("${aci.client.id}") String clientId,
      @Value("${aci.client.secret}") String clientSecret,
      @Value("${aci.client.grantType}") String grantType,
      RestTemplate restTemplate) {
    this.aciTokenUrl = aciTokenUrl;
    this.authKey = authKey;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.grantType = grantType;
    this.restTemplate = restTemplate;
  }

  @Override
  @Cacheable(value = "AciTokenCache")
  public String getAciToken(String methodName) {
    methodName =
        methodName.replaceFirst(Constants.REGEX_REPLACE_CLASS, this.getClass().getSimpleName());
    methodName =
        methodName.replaceFirst(
            Constants.REGEX_REPLACE_METHOD,
            Thread.currentThread().getStackTrace()[1].getMethodName());
    log.info("{} message: method entry", methodName);
    log.info("{} message: Start getting ACI token", methodName);
    String tokenBody =
        String.format(
            "client_id=%s&client_secret=%s&grant_type=%s", clientId, clientSecret, grantType);
    HttpHeaders headers = new HttpHeaders();
    headers.add(Constants.X_AUTH_KEY, authKey);
    headers.add(HttpHeaders.CONTENT_TYPE, Constants.ACI_TOKEN_CONTENT_TYPE);
    HttpEntity<String> httpEntity = new HttpEntity<>(tokenBody, headers);
    ResponseEntity<AciTokenResponse> tokenResponse =
        restTemplate.exchange(aciTokenUrl, HttpMethod.POST, httpEntity, AciTokenResponse.class);
    AciTokenResponse aciTokenResponse = tokenResponse.getBody();
    if (aciTokenResponse == null || StringUtils.isBlank(aciTokenResponse.getAccessToken())) {
      throw new InternalValidationException(
          String.format("Failed to get OAuth2 token from ACI %s", aciTokenUrl));
    }
    log.info(
        "{} message: Getting oauth2 token from ACI successfully on {}", methodName, aciTokenUrl);
    return aciTokenResponse.getAccessToken();
  }

  @Override
  @Caching(evict = {@CacheEvict(value = "AciTokenCache", allEntries = true)})
  // Evict cache every 45 minutes
  @Scheduled(fixedRate = 45, timeUnit = TimeUnit.MINUTES)
  public void cleanupAciTokenCache() {
    log.info("Evicted AciTokenCache cache at {}", ZonedDateTime.now(ZoneId.of("America/New_York")));
  }

  @Override
  public void dryRun(String methodName) {
    methodName =
        methodName.replaceFirst(Constants.REGEX_REPLACE_CLASS, this.getClass().getSimpleName());
    methodName =
        methodName.replaceFirst(
            Constants.REGEX_REPLACE_METHOD,
            Thread.currentThread().getStackTrace()[1].getMethodName());
    log.info("{} message: method entry", methodName);
    getAciToken(methodName);
  }
}
