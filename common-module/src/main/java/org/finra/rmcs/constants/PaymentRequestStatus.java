package org.finra.rmcs.constants;

public enum PaymentRequestStatus {

    VALID(1) {
        @Override
        public PaymentRequestStatus nextState() {
            return PaymentRequestStatus.VALID;
        }
    },

    INVALID(2) {
        @Override
        public PaymentRequestStatus nextState() {
            return PaymentRequestStatus.INVALID;
        }
    },
    EXPIRED(3){
        @Override
        public PaymentRequestStatus nextState() {
            return PaymentRequestStatus.EXPIRED;
        }
    };
    private Integer id;

    PaymentRequestStatus(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public static PaymentRequestStatus getEnumById(Integer id) {
        for (PaymentRequestStatus e : values()) {
            if (e.id != null && e.id.equals(id)) {
                return e;
            }
        }
        return null;
    }

    public static Integer getNextStatusId(Integer id) {
        PaymentRequestStatus status = getEnumById(id);
        if (status != null) {
            return status.nextState().id;
        }
        return id;
    }

    public abstract PaymentRequestStatus nextState();
}




