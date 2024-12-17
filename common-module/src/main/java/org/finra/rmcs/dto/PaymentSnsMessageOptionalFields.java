package org.finra.rmcs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Used to handle Situation that Invoice Payment not even reached to payment service b/c validation
 * failed. As a result, Autopay config needs to be soft deleted and email sent
 * 
 * @author K32328
 *
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSnsMessageOptionalFields {
  @JsonProperty("org_id")
  private String orgId;

  @JsonProperty("business_unit")
  private String businessUnit;

  @JsonProperty("saaEmail")
  private String saaEmail;

  @JsonProperty("email")
  private String email;
}
