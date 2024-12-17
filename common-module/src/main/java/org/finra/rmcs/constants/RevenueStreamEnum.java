package org.finra.rmcs.constants;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RevenueStreamEnum {
  // Add more when needed
  APIBI, COBRA, ADVRG, MQPBI, IARCE, IARCE_FORM, CRDRG, EMSBU, CPACT, CRDRN, DROCL, DRCAT, EBILL_INVOICES, ADFBI, ADFRF, CMABI, EDUCA, FREGN, GASBE, MREGN, ORFBI, TAFBI, TRACE, MTRCS,
  CATFB, Renewal, FNC, RegT, RGFEE, ATRBI;

  public static RevenueStreamEnum getRevenueStreamEnum(String revenueStream) {
    List<RevenueStreamEnum> list = Stream.of(RevenueStreamEnum.values())
        .filter(f -> f.name().equalsIgnoreCase(revenueStream)).collect(Collectors.toList());
    return list.size() > 0 ? list.get(0) : null;
  }
}
