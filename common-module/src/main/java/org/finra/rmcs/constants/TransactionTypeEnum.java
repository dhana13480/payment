package org.finra.rmcs.constants;

public enum TransactionTypeEnum {

    OPAY("OPAY"),
    AUTOPAY("AUTOPAY") ,
    ADDFUNDS("ADDFUNDS"),
    EBILL_INVOICES("EBILL_INVOICES"),
    INVOICE_AUTOPAY("INVOICE_AUTOPAY"),
    EBILL_RENEWAL("EBILL_RENEWAL");

    private String transactionType;

    TransactionTypeEnum(String transactionType){
        this.transactionType = transactionType;
    }

}
