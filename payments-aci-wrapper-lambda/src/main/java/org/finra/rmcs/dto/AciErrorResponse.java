package org.finra.rmcs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("error")
public class AciErrorResponse {

  @JsonProperty("status")
  private Integer status;

  @JsonProperty("kind")
  private String kind;

  @JsonProperty("message")
  private Message message;

  @JsonProperty("details")
  private List<AciErrorResponseDetails> details = new ArrayList<>();
}
