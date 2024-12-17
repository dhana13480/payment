package org.finra.rmcs;

import org.finra.rmcs.constants.EventNameEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventNameEnumTest {
    @Test
    public void testGetEventNameEnum(){
        EventNameEnum eventNameEnum = EventNameEnum.getEventNameEnum("INVOICE_AUTOPAY_DELETE_SYSTEM_INITIATE");
        Assertions.assertNotNull(eventNameEnum);
        Assertions.assertEquals("INVOICE_AUTOPAY_DELETE_SYSTEM_INITIATE",eventNameEnum.name());
    }
}
