package org.finra.rmcs;

import org.finra.rmcs.constants.PaymentEvent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PaymentEventTest {

    @Test
    public void testGetEvent(){
        PaymentEvent paymentEvent = PaymentEvent.getEvent("SYSTEM_FAILURE");
        Assertions.assertNotNull(paymentEvent);
        Assertions.assertEquals("SYSTEM_FAILURE",paymentEvent.name());
    }
}
