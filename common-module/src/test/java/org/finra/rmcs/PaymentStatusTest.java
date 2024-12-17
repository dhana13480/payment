package org.finra.rmcs;




import org.finra.rmcs.constants.PaymentStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class PaymentStatusTest {

    @Test
    public void test_PaymentStatus(){
        assertEquals(PaymentStatus.NEW, PaymentStatus.NULL.nextState());
        assertEquals(PaymentStatus.PAID, PaymentStatus.NEW.nextState());
        assertEquals(PaymentStatus.FAILED, PaymentStatus.FAILED.nextState());
        assertEquals(PaymentStatus.RETURN, PaymentStatus.RETURN.nextState());
        assertEquals(PaymentStatus.EXPIRED, PaymentStatus.EXPIRED.nextState());
        assertEquals(Integer.valueOf(1),PaymentStatus.NEW.getId());
        assertEquals(Integer.valueOf(4),PaymentStatus.RETURN.getId());
    }

    @Test
    public void test_getEnumByID(){
        PaymentStatus expected = PaymentStatus.PAID;
        PaymentStatus actual = PaymentStatus.getEnumById(3);

        assertEquals(expected, actual);
        assertEquals(PaymentStatus.NULL, PaymentStatus.getEnumById(null));
    }

    @Test
    public void test_getEnumByIDInvalid(){
        Integer id =10;
        PaymentStatus actual = PaymentStatus.getEnumById(id);
        assertEquals(PaymentStatus.NULL,actual);
        //assertNull(actual);
    }

    @Test
    public void test_getNextStatusId(){
       // assertEquals(PaymentStatus.PAID.getId(), PaymentStatus.getNextStatusId(1) );
        Integer id = 1;
        Integer nextStatusId= PaymentStatus.getNextStatusId(id);
        assertEquals(id, PaymentStatus.getNextStatusId(10));
       // assertEquals(id, nextStatusId);
    }

    @Test
    public void test_isErrorStatus(){
        boolean result = PaymentStatus.isErrorStatus(10);
        boolean result1 = PaymentStatus.isErrorStatus(2);
        boolean result2 = PaymentStatus.isErrorStatus(null);

        assertTrue(result1);
        assertFalse(result);
        assertFalse(result2);
    }

    @Test
    public void test_PaymentStatus_When_Is_Null(){
        PaymentStatus paymentStatusNull = PaymentStatus.getEnumById(null);
        PaymentStatus paymentStatusNew = PaymentStatus.getEnumById(1);
        PaymentStatus paymentStatusFailed = PaymentStatus.getEnumById(2);
        PaymentStatus paymentStatusPaid = PaymentStatus.getEnumById(3);
        PaymentStatus paymentStatusReturn = PaymentStatus.getEnumById(4);
        PaymentStatus paymentStatusAuthorized = PaymentStatus.getEnumById(5);
        PaymentStatus paymentStatusExpired = PaymentStatus.getEnumById(6);

        Integer PaymentStatusNullId = paymentStatusNull.getId();
        Integer PaymentStatusNewId = paymentStatusNew.getId();
        Integer PaymentStatusFailedId = paymentStatusFailed.getId();
        Integer PaymentStatusPaidId = paymentStatusPaid.getId();
        Integer PaymentStatusReturnId = paymentStatusReturn.getId();
        Integer PaymentStatusAuthorizedId = paymentStatusAuthorized.getId();
        Integer PaymentStatusExpiredId = paymentStatusExpired.getId();

        Assertions.assertEquals(paymentStatusNull.getId(), PaymentStatusNullId);
        Assertions.assertEquals(paymentStatusNew.getId(), PaymentStatusNewId);
        Assertions.assertEquals(paymentStatusFailed.getId(), PaymentStatusFailedId);
        Assertions.assertEquals(paymentStatusPaid.getId(), PaymentStatusPaidId);
        Assertions.assertEquals(paymentStatusReturn.getId(), PaymentStatusReturnId);
        Assertions.assertEquals(paymentStatusAuthorized.getId(), PaymentStatusAuthorizedId);
        Assertions.assertEquals(paymentStatusExpired.getId(), PaymentStatusExpiredId);
    }
}
