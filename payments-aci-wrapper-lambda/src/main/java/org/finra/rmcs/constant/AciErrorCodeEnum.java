package org.finra.rmcs.constant;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public enum AciErrorCodeEnum {
  // 400
  INVALID_REQUEST("W.713.1000"),

  // 401
  UNAUTHORIZED("W.713.3000"),
  INVALID_CREDENTIALS("W.713.3001"),
  EXPIRED_TOKEN("W.713.3004"),
  INVALID_TOKEN("W.713.3005"),

  // 403
  OPERATION_DECLINED("W.713.4000"),
  CARD_DECLINED("W.713.4001"),
  DUPLICATE_FUNDING_ACCOUNT("W.713.4002"),
  MAX_NUMBER_OF_FUNDING_ACCOUNTS_EXCEEDED("W.713.4003"),
  RESTRICTED_FUNDING("W.713.4004"),
  INCORRECT_PERMISSION("W.713.4101"),
  INCORRECT_OWNER("W.713.4102"),
  OPERATION_DECLINED_DETAILS("W.713.4900"),

  // 404
  RESOURCE_NOT_FOUND("W.713.1404"),
  RESOURCE_NOT_FOUND_DETAILS("W.713.14040"),
  FUNDING_NOT_FOUND("W.713.14041"),
  USER_NOT_FOUND("W.713.14042"),
  BILLING_ACCOUNT_NOT_FOUND("W.713.14043"),
  BILLING_ACCOUNT_TOKEN_NOT_FOUND("W.713.14044"),

  // 422
  VALIDATION_ERROR("W.713.1300"),
  INVALID_FIELD_FORMAT("W.713.1301", "%s has invalid format."),
  INVALID_FIELD_SIZE("W.713.1302", "%s has invalid size."),
  REQUIRED_FIELD("W.713.1303", "%s is required."),
  REDUNDANT_FIELD("W.713.1304", "%s is redundant."),
  INVALID_FIELD_VALUE("W.713.1305", "%s is not one of the possible values."),
  FIELD_OUT_OF_RANGE("W.713.1307", "%s has invalid size."),
  BLOCKED_BIN("W.713.1310"),
  MOD_10_FAILED("W.713.1311"),
  CARD_EXPIRED("W.713.1312"),
  RESTRICTED_ROUTING("W.713.1313"),
  INVALID_ROUTING("W.713.1314"),
  INACTIVE_ROUTING("W.713.1315"),
  REPLACED_ROUTING("W.713.1316"),
  INVALID_ROUTING_2("W.713.1316"),
  INVALID_BANK("W.713.1318"),
  ECHECK_INVALID_LVL1("W.713.1319"),
  ECHECK_INVALID_LVL2("W.713.1320"),
  ACH_TRANS_TYPE_DISABLED("W.713.1321"),
  PAYMENT_METHOD_DISABLED("W.713.1322"),
  BRAND_DISABLED("W.713.1323"),
  BILLING_ACCOUNT_INCORRECT("W.713.1324"),
  AUTHORIZATION_FAILED(
      "W.713.1325",
      "Bank account authorization declined. This routing or bank account number is invalid. Please enter different account numbers or pick a different payment method."),
  EXPIRATION_DATE_ERROR("W.713.1326"),
  INVALID_SECURITY_CODE("W.713.1327"),
  FUNDING_ACCOUNT_INVALID("W.713.1328"),
  PAYMENT_TOKEN_INVALID("W.713.1329"),
  INVALID_BILLING_ACCOUNT_TOKEN("W.713.1344"),
  VALIDATION_ERROR_DETAILS("W.713.1399"),
  CARD_DATA_INVALID("W.713.23291");

  private String errorCode;
  private String errorMessageFormat;

  AciErrorCodeEnum(String errorCode) {
    this.errorCode = errorCode;
  }

  AciErrorCodeEnum(String errorCode, String errorMessageFormat) {
    this.errorCode = errorCode;
    this.errorMessageFormat = errorMessageFormat;
  }

  public static AciErrorCodeEnum getErrorByErrorCode(String errorCode) {
    List<AciErrorCodeEnum> list =
        Arrays.stream(AciErrorCodeEnum.values())
            .filter(f -> f.getErrorCode().equals(errorCode))
            .toList();
    return !list.isEmpty() ? list.get(0) : null;
  }

  public String getErrorCode() {
    return this.errorCode;
  }

  public String getErrorMessageFormat() {
    return this.errorMessageFormat;
  }

  public boolean hasErrorMessageFormat() {
    return StringUtils.isNotBlank(this.errorMessageFormat);
  }
}
