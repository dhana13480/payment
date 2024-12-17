package org.finra.rmcs.exception;

import java.util.List;

public class InternalValidationException extends RuntimeException {

  private List<String> violations;

  public InternalValidationException(List<String> violations) {
    this.violations = violations;
  }

  public InternalValidationException(String message) {
    super(message);
  }

  public List<String> getViolations() {
    return this.violations;
  }
}
