package org.finra.rmcs.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentClaimCheckResponse {
  @JsonProperty("message")
  private String message;

  @JsonProperty("errors")
  private List<String> errors;
}
