package org.finra.rmcs.constants;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EventNameEnum {
  // From payment status
  NEW,
  PAID,
  FAILED,
  RETURN,
  AUTHORIZED,
  REMINDER,
  EXPIRED,
  // From payment reason
  CHARGE_BACK,
  CHARGE_BACK_RETURN,
  ACH_RETURN,
  REIMBURSED,
  RECONCILIATION_ERROR,
  SYSTEM_FAILURE,
  INVOICE_AUTOPAY_DELETE_SYSTEM_INITIATE,
  INVOICE_AUTOPAY_DELETE_PAYMENT_FAILED;

  public static EventNameEnum getEventNameEnum(String eventName) {
    List<EventNameEnum> list =
        Stream.of(EventNameEnum.values())
            .filter(f -> f.name().equals(eventName))
            .collect(Collectors.toList());
    return !list.isEmpty() ? list.get(0) : null;
  }
}
