package org.finra.rmcs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.finra.rmcs.constant.AciErrorCodeEnum;
import org.finra.rmcs.constant.AciTargetEnum;
import org.finra.rmcs.constant.Constants;
import org.finra.rmcs.constants.PaymentTypes;
import org.finra.rmcs.dto.AciErrorResponse;
import org.finra.rmcs.dto.AciErrorResponseDetails;
import org.finra.rmcs.exception.InternalValidationException;
import org.springframework.web.client.HttpStatusCodeException;

public class Utils {

  private Utils() {
    throw new IllegalStateException(" all are static methods ");
  }

  public static String generateUrl(String url, Map<String, Object> request) {
    url = url + "/billeraccounts/%s/%s/fundingaccounts";
    request = (Map<String, Object>) request.get("owner");
    String billerId = String.valueOf(request.get("billerId"));
    String billerAccountId = String.valueOf(request.get("billerAccountId"));
    return String.format(url, billerId, billerAccountId);
  }

  public static void validateRequest(Map<String, Object> requestEvent) {
    String kind = String.valueOf(requestEvent.getOrDefault("kind", StringUtils.EMPTY));

    List<String> violations = new ArrayList<>();
    Set<String> keys = requestEvent.keySet();

    if (kind.equals(PaymentTypes.ACH.getPaymentType())) {
      for (String field : Constants.achRequiredFields) {
        if (!keys.contains(field)) {
          violations.add(String.format("%s is required.", field));
        }
      }
    } else if (kind.equals(PaymentTypes.Card.getPaymentType())) {
      for (String field : Constants.ccRequiredFields) {
        if (!keys.contains(field)) {
          violations.add(String.format("%s is required.", field));
        }
      }
    } else {
      violations.add("kind is missing or unknown,");
      return;
    }

    Map<String, Object> owner =
        (Map<String, Object>) requestEvent.getOrDefault("owner", new HashMap<>());
    Set<String> ownerKeys = owner.keySet();
    for (String field : Constants.ownerRequiredFields) {
      if (!ownerKeys.contains(field)) {
        violations.add(String.format("owner.%s is required.", field));
      }
    }

    Map<String, Object> billingAddress =
        (Map<String, Object>) requestEvent.getOrDefault("billingAddress", new HashMap<>());

    Set<String> billingAddressKeys = billingAddress.keySet();
    for (String field : Constants.billingAddressRequiredFields) {
      if (!billingAddressKeys.contains(field)) {
        violations.add(String.format("billingAddress.%s is required.", field));
      }
    }
    if (!violations.isEmpty()) {
      throw new InternalValidationException(violations);
    }
  }

  public static Map<String, Object> covertAciErrorResponse(HttpStatusCodeException e) {
    List<String> errors = new ArrayList<>();
    if (e.getStatusCode().is5xxServerError()) {
      errors.add(Constants.GENERAL_ERROR_MESSAGE);
      return getErrorReturnMap(errors);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
    objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);

    if (e.getStatusCode().is4xxClientError()) {
      AciErrorResponse aciErrorResponse;
      try {
        aciErrorResponse =
            objectMapper.readValue(e.getResponseBodyAsString(), AciErrorResponse.class);
      } catch (JsonProcessingException ex) {
        errors.add(e.getResponseBodyAsString());
        return getErrorReturnMap(errors);
      }
      List<AciErrorResponseDetails> aciErrorResponseDetailsList = aciErrorResponse.getDetails();

      if (aciErrorResponseDetailsList.isEmpty()) {
        errors.add(aciErrorResponse.getMessage().getDefaultValue());
        return getErrorReturnMap(errors);
      }

      for (AciErrorResponseDetails aciErrorResponseDetails : aciErrorResponseDetailsList) {
        AciErrorCodeEnum aciErrorCode =
            AciErrorCodeEnum.getErrorByErrorCode(aciErrorResponseDetails.getMessage().getCode());
        if (aciErrorCode == null) {
          errors.add(Constants.GENERAL_ERROR_MESSAGE);
        } else {
          if (aciErrorCode.hasErrorMessageFormat()) {
            if (aciErrorCode.getErrorMessageFormat().contains("%s")) {
              if (StringUtils.isNotBlank(aciErrorResponseDetails.getTarget())) {
                AciTargetEnum aciTargetEnum =
                    AciTargetEnum.getErrorByAciTarget(aciErrorResponseDetails.getTarget());
                errors.add(
                    String.format(
                        aciErrorCode.getErrorMessageFormat(),
                        aciTargetEnum != null
                            ? aciTargetEnum.getUILabel()
                            : aciErrorResponseDetails.getTarget()));
              } else {
                errors.add(aciErrorResponseDetails.getMessage().getDefaultValue());
              }
            } else {
              errors.add(aciErrorCode.getErrorMessageFormat());
            }
          } else {
            errors.add(aciErrorResponseDetails.getMessage().getDefaultValue());
          }
        }
      }
    }
    return getErrorReturnMap(errors.stream().distinct().toList());
  }

  public static Map<String, Object> getErrorReturnMap(List<String> errors) {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put("errors", errors);
    return returnMap;
  }
}
