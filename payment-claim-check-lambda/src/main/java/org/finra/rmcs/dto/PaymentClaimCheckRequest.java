package org.finra.rmcs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentClaimCheckRequest {
  // Assume request coming from payment gateway, and will be in this format:
  // {
  //   "params": {
  //     "path": {
  //       "s3_file_key": "esmp_notification/MQPBI/f027585b-cb10-4081-a251-d13a2c0cd7c1/2023-06-20T10:45:39.970010300"
  //      }
  //   },
  //   "context": {
  //     "id_token": ""
  //   }
  // }

  @JsonProperty("params")
  private PaymentClaimCheckRequestParams params;


  @JsonProperty("context")
  private TokenContext context;
}
