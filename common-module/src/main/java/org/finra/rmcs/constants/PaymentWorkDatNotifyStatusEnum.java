package org.finra.rmcs.constants;

public enum PaymentWorkDatNotifyStatusEnum {
  READY(1) ,
  COMPLETED(2) ;

  Integer id;
  PaymentWorkDatNotifyStatusEnum(int id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }
}
