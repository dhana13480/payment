package org.finra.rmcs.constants;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PaymentEvent {
  NEW,
  PAID,
  FAILED,
  RETURN,
  CHARGE_BACK,
  CHARGE_BACK_RETURN,
  ACH_RETURN,
  REIMBURSED,
  RECONCILIATION_ERROR,
  REMINDER,
  EXPIRED,
  SYSTEM_FAILURE;

  public static PaymentEvent getEvent(String eventName) {
    List<PaymentEvent> list =
        Stream.of(PaymentEvent.values())
            .filter(f -> f.name().equals(eventName))
            .collect(Collectors.toList());
    return !list.isEmpty() ? list.get(0) : null;
  }
}
