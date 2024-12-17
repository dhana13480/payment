package org.finra.rmcs.function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.finra.rmcs.constant.Constants;
import org.finra.rmcs.exception.InternalValidationException;
import org.finra.rmcs.service.FetchCountryService;
import org.finra.rmcs.service.FundingTokenService;
import org.finra.rmcs.util.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
@Component
@AllArgsConstructor
public class PaymentsAciWrapperFunction
    implements Function<Map<String, Object>, ResponseEntity<Map<String, Object>>> {

  private final FundingTokenService fundingTokenService;
  private final FetchCountryService fetchCountryService;

  @SneakyThrows
  @Override
  public ResponseEntity<Map<String, Object>> apply(Map<String, Object> requestEvent) {
    String correlationId = UUID.randomUUID().toString();
    String methodName =
        Constants.CLASS
            + this.getClass().getSimpleName()
            + " "
            + Constants.METHOD
            + Thread.currentThread().getStackTrace()[1].getMethodName()
            + " "
            + Constants.CORRELATION_ID_LOG
            + correlationId;
    log.info("{} message: method entry", methodName);
    ResponseEntity<Map<String, Object>> response;
    Map<String, Object> returnMap = new HashMap<>();
    try {
      // Fetch Country logic
      String requestPath =
          String.valueOf(requestEvent.getOrDefault(Constants.REQUEST_PATH, StringUtils.EMPTY));
      if (requestPath.equalsIgnoreCase("/payment/countries")) {
        log.info("{} message: Start getting a list of countries from ACI.", methodName);
        response = fetchCountryService.getCountries(methodName);
        log.info("{} message: Getting a list of countries from ACI successfully.", methodName);
        return response;
      } else if (requestPath.equalsIgnoreCase("/payment/countries/{countryCode}")) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.convertValue(requestEvent, JsonNode.class);
        String country = json.get("params").get("path").get("countryCode").asText();
        if (Constants.DRY_RUN.equalsIgnoreCase(country)) {
          log.info("{} message: Dry run mode on /payment/countries/{countryCode}", methodName);
          fetchCountryService.dryRun(methodName);
          log.info("{} message: Dry run success on /payment/countries/{countryCode}", methodName);
          returnMap.put(Constants.MESSAGE, Constants.DRY_RUN_PROCESSED_SUCCESSFULLY);
          return new ResponseEntity<>(returnMap, HttpStatus.OK);
        }
        log.info("{} message: Start getting a Country {} details from ACI.", methodName, country);
        response = fetchCountryService.getCountryDetails(methodName, country);
        log.info(
            "{} message: Getting getting a Country {} details from ACI successfully.",
            methodName,
            country);
        return response;
      }

      // Funding token logic
      if (requestEvent.get(Constants.BODY_JSON) != null) {
        requestEvent = (Map<String, Object>) requestEvent.get(Constants.BODY_JSON);
      }

      boolean dryRun =
          Boolean.parseBoolean(
              requestEvent.getOrDefault(Constants.DRY_RUN, Constants.FALSE).toString());

      if (dryRun) {
        log.info("{} message: Dry run mode on /payment/fundingtoken", methodName);
        fundingTokenService.dryRun(methodName);
        log.info("{} message: Dry run success on /payment/fundingtoken", methodName);
        returnMap.put(Constants.MESSAGE, Constants.DRY_RUN_PROCESSED_SUCCESSFULLY);
        return new ResponseEntity<>(returnMap, HttpStatus.OK);
      }

      String transmissionId =
          requestEvent.getOrDefault(Constants.TRANSMISSION_ID, StringUtils.EMPTY).toString();
      String paymentReqId =
          requestEvent.getOrDefault(Constants.PAYMENT_REQ_ID, StringUtils.EMPTY).toString();
      if (StringUtils.isNotBlank(transmissionId)) {
        log.info("{} message: Updating correlationId to {}", methodName, transmissionId);
        methodName = methodName.replace(correlationId, transmissionId);
      }
      if (StringUtils.isNotBlank(paymentReqId)) {
        methodName = methodName + " " + Constants.PAYMENT_REQ_ID_LOG + paymentReqId;
      }
      Utils.validateRequest(requestEvent);
      log.info("{} message: Start getting funding token from ACI", methodName);
      requestEvent.remove(Constants.TRANSMISSION_ID);
      requestEvent.remove(Constants.PAYMENT_REQ_ID);
      response = fundingTokenService.getFundingToken(methodName, requestEvent);
      log.info("{} message: Getting funding token from ACI successfully", methodName);
    } catch (InternalValidationException e) {
      log.info("{} message: Internal validation failed: {}", methodName, e.getViolations());
      return new ResponseEntity<>(
          Utils.getErrorReturnMap(e.getViolations()), HttpStatus.BAD_REQUEST);
    } catch (HttpStatusCodeException e) {
      log.info(
          Constants.HTTP_STATUS_CODE_EXCEPTION_LOG_FORMAT,
          methodName,
          e.getStatusCode(),
          e.getResponseBodyAsString());
      return new ResponseEntity<>(Utils.covertAciErrorResponse(e), e.getStatusCode());
    } catch (Exception e) {
      log.info(
          "{} message: Unexpected exception occurred {}",
          methodName,
          ExceptionUtils.getStackTrace(e));
      returnMap.put(Constants.MESSAGE, ExceptionUtils.getStackTrace(e));
      return new ResponseEntity<>(returnMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
  }
}
