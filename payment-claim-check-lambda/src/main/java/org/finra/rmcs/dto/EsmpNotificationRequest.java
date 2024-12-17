package org.finra.rmcs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EsmpNotificationRequest {

  @JsonProperty("client_application_name")
  private String clientApplicationName;

  @JsonProperty("external_id")
  private String externalId;

  @JsonProperty("status")
  private String status;

  @JsonProperty("buyer_user_name")
  private String buyerUserName;

  @JsonProperty("buyer_first_name")
  private String buyerFirstName;

  @JsonProperty("buyer_last_name")
  private String buyerLastName;

  @JsonProperty("buyer_email")
  private String buyerEmail;

  @JsonProperty("correlation_id")
  private String correlationId;

  @JsonProperty("extra_bill_information")
  private String extraBillInformation;

  @JsonProperty("failure_reason")
  private String failureReason;

  @JsonProperty("net_purchase_amount")
  private BigDecimal netPurchaseAmount;

  @JsonProperty("payment_type")
  private String paymentType;

  @JsonProperty("payment_account_type")
  private String paymentAccountType;

  @JsonProperty("purchase_price")
  private BigDecimal purchasePrice;

  @JsonProperty("purchase_price_convenience_fee")
  private BigDecimal purchasePriceConvenienceFee;

  @JsonProperty("purchase_price_convenience_fee_waiver_reason")
  private String purchasePriceConvenienceFeeWaiverReason;

  @JsonProperty("submit_timestamp")
  private LocalDateTime submitTimestamp;

  @JsonProperty("update_timestamp")
  private LocalDateTime updateTimestamp;

  @JsonProperty("trigger_process")
  private String triggerProcess;

  @JsonProperty("trigger_timestamp")
  private LocalDateTime triggerTimestamp;

  @JsonProperty("reversal_original_payment_date")
  private LocalDateTime reversalOriginalPaymentDate;

  @JsonProperty("reversal_original_confirmation")
  private String reversalOriginalConfirmation;

  @JsonProperty("reversal_return_code")
  private String reversalReturnCode;

  @JsonProperty("reversal_return_code_message")
  private String reversalReturnCodeMessage;

  @JsonProperty("reversal_confirmation")
  private String reversalConfirmation;

}
