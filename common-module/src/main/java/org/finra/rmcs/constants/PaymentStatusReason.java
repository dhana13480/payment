package org.finra.rmcs.constants;

public enum PaymentStatusReason {
    NULL(null),
    CHARGE_BACK(1),
    CHARGE_BACK_RETURN(2),
    ACH_RETURN(3),
    REIMBURSED(4);

    private Integer id;

    PaymentStatusReason(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static PaymentStatusReason getEnumById(Integer id) {
        for (PaymentStatusReason e: values()) {
            if ((null != id) && (null != e.id) && (e.id.equals(id))) {
              return e;
            }
        }

        return PaymentStatusReason.NULL;
    }
}
