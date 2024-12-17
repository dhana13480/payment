package org.finra.rmcs.service;

import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface FundingTokenService {

  ResponseEntity<Map<String, Object>> getFundingToken(
      String methodName, Map<String, Object> request);

  void dryRun(String methodName);
}
