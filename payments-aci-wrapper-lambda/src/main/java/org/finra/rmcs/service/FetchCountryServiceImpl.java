package org.finra.rmcs.service;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.finra.rmcs.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class FetchCountryServiceImpl implements FetchCountryService {

  private final GetAciTokenServiceImpl getAciTokenService;
  private final RestTemplate restTemplate;
  private final String countryUrl;
  private final String authKey;

  @Autowired
  public FetchCountryServiceImpl(
      RestTemplate restTemplate,
      GetAciTokenServiceImpl getAciTokenService,
      @Value("${aci.client.countryUrl}") String countryUrl,
      @Value("${aci.client.x-auth-key}") String authKey) {
    this.restTemplate = restTemplate;
    this.getAciTokenService = getAciTokenService;
    this.countryUrl = countryUrl;
    this.authKey = authKey;
  }

  @Override
  public ResponseEntity<Map<String, Object>> getCountries(String methodName) {
    methodName =
        methodName.replaceFirst(Constants.REGEX_REPLACE_CLASS, this.getClass().getSimpleName());
    methodName =
        methodName.replaceFirst(
            Constants.REGEX_REPLACE_METHOD,
            Thread.currentThread().getStackTrace()[1].getMethodName());
    log.info("{} message: method entry", methodName);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("X-Auth-Key", authKey);
    httpHeaders.set("Content-Type", Constants.APPLICATION_JSON);
    httpHeaders.set("Accept", Constants.APPLICATION_JSON);
    httpHeaders.setBearerAuth(getAciTokenService.getAciToken(methodName));

    return restTemplate.exchange(
        countryUrl,
        HttpMethod.GET,
        new HttpEntity<>(httpHeaders),
        new ParameterizedTypeReference<>() {});
  }

  @Override
  public ResponseEntity<Map<String, Object>> getCountryDetails(String methodName, String country) {
    methodName =
        methodName.replaceFirst(Constants.REGEX_REPLACE_CLASS, this.getClass().getSimpleName());
    methodName =
        methodName.replaceFirst(
            Constants.REGEX_REPLACE_METHOD,
            Thread.currentThread().getStackTrace()[1].getMethodName());
    log.info("{} message: method entry", methodName);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("X-Auth-Key", authKey);
    httpHeaders.set("Content-Type", Constants.APPLICATION_JSON);
    httpHeaders.set("Accept", Constants.APPLICATION_JSON);
    httpHeaders.setBearerAuth(getAciTokenService.getAciToken(methodName));

    String requestUrl = String.format("%s/%s", countryUrl, country);
    return restTemplate.exchange(
        requestUrl,
        HttpMethod.GET,
        new HttpEntity<>(httpHeaders),
        new ParameterizedTypeReference<>() {});
  }

  @Override
  public void dryRun(String methodName) {
    log.info("{} message: method entry", methodName);
    getCountries(methodName);
    getCountryDetails(methodName, "USA");
  }
}
