package org.finra.rmcs.constants;

import java.util.Optional;
import java.util.Arrays;
public enum PaymentTypes {

    ACH("Ach"),
    Card("Card");

    private String paymentType;

    PaymentTypes(String paymentType){
        this.paymentType = paymentType;
    }

    public String getPaymentType(){
        return paymentType;
    }

    public static String get(String pType) {
        Optional<PaymentTypes> result =  Arrays.stream(PaymentTypes.values())
            .filter(type -> type.paymentType.equalsIgnoreCase(pType))
            .findFirst();

        if(result.isPresent()){
            return String.valueOf(result.get());
        }
        return  null;
    }
}




