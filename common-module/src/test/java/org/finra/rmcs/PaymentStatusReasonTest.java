package org.finra.rmcs;


import org.finra.rmcs.constants.PaymentStatusReason;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PaymentStatusReasonTest {

    @Test
    public void test_PaymentStatusReason(){
       assertEquals(PaymentStatusReason.NULL, PaymentStatusReason.getEnumById(PaymentStatusReason.NULL.getId()));
       assertEquals(PaymentStatusReason.CHARGE_BACK, PaymentStatusReason.getEnumById(PaymentStatusReason.CHARGE_BACK.getId()));
       assertEquals(PaymentStatusReason.CHARGE_BACK_RETURN, PaymentStatusReason.getEnumById(PaymentStatusReason.CHARGE_BACK_RETURN.getId()));
       assertEquals(PaymentStatusReason.ACH_RETURN, PaymentStatusReason.getEnumById(PaymentStatusReason.ACH_RETURN.getId()));
       assertEquals(PaymentStatusReason.REIMBURSED, PaymentStatusReason.getEnumById(PaymentStatusReason.REIMBURSED.getId()));
    }
}
