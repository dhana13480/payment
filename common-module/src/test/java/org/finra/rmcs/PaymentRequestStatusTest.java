package org.finra.rmcs;

import org.finra.rmcs.constants.PaymentRequestStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class PaymentRequestStatusTest {

   @Test
   public void test_PaymentRequestStatus(){
       assertEquals(Integer.valueOf(1), PaymentRequestStatus.VALID.getId());
       assertEquals(PaymentRequestStatus.VALID, PaymentRequestStatus.VALID.nextState());
       assertEquals(Integer.valueOf(2), PaymentRequestStatus.INVALID.getId());
       assertEquals(PaymentRequestStatus.INVALID, PaymentRequestStatus.INVALID.nextState());

    }

    @Test
    public void test_getEnumByID(){
        PaymentRequestStatus expected = PaymentRequestStatus.VALID;
        PaymentRequestStatus actual = PaymentRequestStatus.getEnumById(1);
        assertEquals(expected, actual);
    }
    @Test
    public void test_getEnumByIDInvalid(){
        PaymentRequestStatus actual = PaymentRequestStatus.getEnumById(10);
        assertNull(actual);
    }
    @Test
    public void test_getNextStatusId(){
        assertEquals(PaymentRequestStatus.VALID.getId(), PaymentRequestStatus.getNextStatusId(1) );
        Integer id = 10;
        assertEquals(id, PaymentRequestStatus.getNextStatusId(10));
    }

}
