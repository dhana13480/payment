package org.finra.rmcs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.finra.rmcs.constant.Constants;
import org.finra.rmcs.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class FundingTokenServiceImpl implements FundingTokenService {

  private final RestTemplate restTemplate;
  private final GetAciTokenServiceImpl getAciTokenService;
  private final String fundingTokenUrl;
  private final String authKey;

  @Autowired
  public FundingTokenServiceImpl(
      RestTemplate restTemplate,
      GetAciTokenServiceImpl getAciTokenService,
      @Value("${aci.client.fundingAccountUrl}") String fundingTokenUrl,
      @Value("${aci.client.x-auth-key}") String authKey) {
    this.restTemplate = restTemplate;
    this.getAciTokenService = getAciTokenService;
    this.fundingTokenUrl = fundingTokenUrl;
    this.authKey = authKey;
  }

  @Override
  public ResponseEntity<Map<String, Object>> getFundingToken(
      String methodName, Map<String, Object> request) {
    methodName =
        methodName.replaceFirst(Constants.REGEX_REPLACE_CLASS, this.getClass().getSimpleName());
    methodName =
        methodName.replaceFirst(
            Constants.REGEX_REPLACE_METHOD,
            Thread.currentThread().getStackTrace()[1].getMethodName());
    log.info("{} message: method entry", methodName);
    String url = Utils.generateUrl(fundingTokenUrl, request);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("X-Auth-Key", authKey);
    httpHeaders.set("Content-Type", Constants.APPLICATION_JSON);
    httpHeaders.set("Accept", Constants.APPLICATION_JSON);
    httpHeaders.setBearerAuth(getAciTokenService.getAciToken(methodName));

    return restTemplate.exchange(
        url,
        HttpMethod.POST,
        new HttpEntity<>(request, httpHeaders),
        new ParameterizedTypeReference<>() {});
  }

  @Override
  @SneakyThrows
  public void dryRun(String methodName) {
    methodName =
        methodName.replaceFirst(Constants.REGEX_REPLACE_CLASS, this.getClass().getSimpleName());
    methodName =
        methodName.replaceFirst(
            Constants.REGEX_REPLACE_METHOD,
            Thread.currentThread().getStackTrace()[1].getMethodName());
    log.info("{} message: method entry", methodName);

    getAciTokenService.dryRun(methodName);
    String json =
        new String(new ClassPathResource("DryRunPayload.json").getInputStream().readAllBytes());
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    try {
      getFundingToken(Constants.DRY_RUN, request);
    } catch (HttpStatusCodeException e) {
      if (!(e.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY))) {
        throw new RuntimeException(e);
      }
    }
  }
}
