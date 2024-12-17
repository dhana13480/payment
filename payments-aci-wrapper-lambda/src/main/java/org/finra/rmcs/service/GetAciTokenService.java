package org.finra.rmcs.service;

public interface GetAciTokenService {

  String getAciToken(String methodName);

  void cleanupAciTokenCache();

  void dryRun(String methodName);
}
