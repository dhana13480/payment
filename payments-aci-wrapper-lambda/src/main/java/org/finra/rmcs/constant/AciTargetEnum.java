package org.finra.rmcs.constant;

import java.util.Arrays;
import java.util.List;

public enum AciTargetEnum {
  ACCOUNT_HOLDER_NAME("accountHolderName", "Full Name"),
  CARD_HOLDER_NAME("cardHolderName", "Full Name"),
  POSTAL_CODE("postalCode", "ZIP code"),
  EXPIRATION_DATE("expirationDate", "Expiration Date");

  private String aciTarget;
  private String uiLabel;

  AciTargetEnum(String aciTarget, String uiLabel) {
    this.aciTarget = aciTarget;
    this.uiLabel = uiLabel;
  }

  public static AciTargetEnum getErrorByAciTarget(String aciTarget) {
    List<AciTargetEnum> list =
        Arrays.stream(AciTargetEnum.values())
            .filter(f -> f.getAciTarget().equals(aciTarget))
            .toList();
    return !list.isEmpty() ? list.get(0) : null;
  }

  public String getAciTarget() {
    return this.aciTarget;
  }

  public String getUILabel() {
    return this.uiLabel;
  }
}
