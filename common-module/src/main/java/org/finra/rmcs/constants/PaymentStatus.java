package org.finra.rmcs.constants;

public enum PaymentStatus {
    /**
     * <p> NULL 
     * PaymentStatus is nullable.
     * This entry addresss the null state.
     * </p>
     */
    NULL(null, false) {
        @Override
        public PaymentStatus nextState() {
            return PaymentStatus.NEW;
        }
    },
    NEW(1, false) {
        @Override
        public PaymentStatus nextState() {
            return PaymentStatus.PAID;
        }
    },
    FAILED(2, true) {
        @Override
        public PaymentStatus nextState() {
            return PaymentStatus.FAILED;
        }
    },
    PAID(3, false) {
        @Override
        public PaymentStatus nextState() {
            return PaymentStatus.PAID;
        }
    },
    RETURN(4, true) {
        @Override
        public PaymentStatus nextState() {
            return PaymentStatus.RETURN;
        }
    },
    EXPIRED(6, false) {
        @Override
        public PaymentStatus nextState() {
            return PaymentStatus.EXPIRED;
        }
    };

    private Integer id;
    private boolean errorStatus;

    PaymentStatus(Integer id, boolean errorStatus) {
        this.id = id;
        this.errorStatus = errorStatus;
    }

    public Integer getId() {
        return id;
    }

    public static PaymentStatus getEnumById(Integer id) {
        for (PaymentStatus e: values()) {
            if ((null != id) && (null != e.id) && (e.id.equals(id))) {
              return e;
            }
        }

        return PaymentStatus.NULL;
    }

    public static Integer getNextStatusId(Integer id) {
        PaymentStatus status = getEnumById(id);
        if (status != null) {
            return status.nextState().id;
        }
        return id;
    }

    public static boolean isErrorStatus(Integer id) {
        PaymentStatus status = getEnumById(id);
        if (status != null) {
            return status.errorStatus;
        }
        return false;
    }

    public abstract PaymentStatus nextState();
}
