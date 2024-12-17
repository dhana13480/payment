package org.finra.rmcs.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtilTest {

    @Test
    public void testConvertToESTNull(){
        Assertions.assertNull(DateTimeUtil.convertToEST(null));
    }

    @Test
    public void testConvertToEST(){
        ZonedDateTime cutOverDateZonedDateTime = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 00000000, ZoneId.of("UTC"));
        String dateTime = DateTimeUtil.convertToEST(cutOverDateZonedDateTime);
        Assertions.assertNotNull(dateTime);
    }

    @Test
    public void testConvertToUtcNull(){
        Assertions.assertNull(DateTimeUtil.convertToUtc(null));
    }

    @Test
    public void testConvertToUtc(){
        Assertions.assertNotNull(DateTimeUtil.convertToUtc(LocalDateTime.now()));
    }
}
