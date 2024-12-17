package org.finra.rmcs.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
public class PageConfiguration {

  @JsonProperty("show_terms_and_conditions")
  private boolean showPaymentTermCondition;

  @JsonProperty("credit_card_limit")
  private String creditCardLimit;

  @JsonProperty("payment_mode")
  private List<String> paymentMode;

  @JsonProperty("header")
  private String header;

  @JsonProperty("note")
  private String note;

  @JsonProperty("charges_column")
  private List<String> chargesColumn;

  @JsonProperty("charges_rows")
  private List<String> chargesRows;
}
