package org.finra.rmcs.dto;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSnsMessage {
  @JsonProperty("payment_req_id")
  private String paymentReqId;

  @JsonProperty("payment_id")
  @Builder.Default
  private List<String> paymentId = new ArrayList<>();

  @JsonProperty("event_name")
  private String eventName;

  @JsonProperty("payment_number")
  @Builder.Default
  private List<String> paymentNumber = new ArrayList<>();

  @JsonProperty("transmission_id")
  private String transmissionId;

  @JsonProperty("ews_user_name")
  private String ewsUserName;

  @JsonProperty("processing_revenue_stream")
  private String processingRevenueStream;

  @JsonProperty("confirmation_number")
  @Builder.Default
  private List<String> confirmationNumber = new ArrayList<>();

  @JsonProperty("invoice_id")
  @Builder.Default
  private List<String> invoiceId = new ArrayList<>();

  @JsonProperty("customer_ids")
  @Builder.Default
  private List<String> customerIds = new ArrayList<>();

  @JsonProperty("transaction_type")
  private String transactionType;

  @JsonProperty("options")
  @Builder.Default
  private PaymentSnsMessageOptionalFields options = new PaymentSnsMessageOptionalFields();
}
