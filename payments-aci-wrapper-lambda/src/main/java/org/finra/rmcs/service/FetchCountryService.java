package org.finra.rmcs.service;

import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface FetchCountryService {

  ResponseEntity<Map<String, Object>> getCountries(String methodName);

  ResponseEntity<Map<String, Object>> getCountryDetails(String methodName, String country);

  void dryRun(String methodName);
}
