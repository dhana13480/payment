package org.finra.rmcs.constants;
public enum EmailTemplates {
    PAID("Paid.html"),
    FAILEDPAYMENTFAILURE("FailedPaymentFailure.html"),
    FAILEDSYSTEMFAILURE("FailedSystemFailure.html"),
    ACHRETURN("ACHReturn.html"),
    CHARGEBACK("Chargeback.html"),
    CHARGEBACKRETURN("ChargebackReturn.html"),
    REIMBURSED("Reimbursed.html"),
    EXECUTORRECONCILIATION("ExecutorReconciliation.html"),
    PAIDCOBRA("PaidCOBRA.html"),
    FAILEDPAYMENTFAILURECOBRA("FailedPaymentFailureCOBRA.html"),
    FAILEDSYSTEMFAILURECOBRA("FailedPaymentFailureCOBRA.html"),
    ACHRETURNCOBRA("ACHReturnCOBRA.html"),
    REIMBURSEDCOBRA("ReimbursedCOBRA.html"),
    EXECUTORRECONCILIATIONCOBRA("ExecutorReconciliationCOBRA.html");

private final String email;

EmailTemplates(String email){
    this.email = email;
}

    public String getEmail() {
        return email;
    }
}
