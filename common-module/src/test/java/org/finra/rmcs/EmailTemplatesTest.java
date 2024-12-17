package org.finra.rmcs;

import org.finra.rmcs.constants.EmailTemplates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EmailTemplatesTest {
    @Test
    public void test_EmailTemplates(){
        assertEquals("Reimbursed.html",EmailTemplates.REIMBURSED.getEmail());
        assertEquals("Paid.html",EmailTemplates.PAID.getEmail());
        assertEquals("FailedPaymentFailure.html",EmailTemplates.FAILEDPAYMENTFAILURE.getEmail());
        assertEquals("FailedSystemFailure.html",EmailTemplates.FAILEDSYSTEMFAILURE.getEmail());
        assertEquals("ACHReturn.html",EmailTemplates.ACHRETURN.getEmail());
        assertEquals("Chargeback.html",EmailTemplates.CHARGEBACK.getEmail());
        assertEquals("ChargebackReturn.html",EmailTemplates.CHARGEBACKRETURN.getEmail());
        assertEquals("ExecutorReconciliation.html",EmailTemplates.EXECUTORRECONCILIATION.getEmail());
        assertEquals("PaidCOBRA.html",EmailTemplates.PAIDCOBRA.getEmail());
        assertEquals("FailedPaymentFailureCOBRA.html",EmailTemplates.FAILEDPAYMENTFAILURECOBRA.getEmail());
        assertEquals("ACHReturnCOBRA.html",EmailTemplates.ACHRETURNCOBRA.getEmail());
        assertEquals("ReimbursedCOBRA.html",EmailTemplates.REIMBURSEDCOBRA.getEmail());
        assertEquals("ExecutorReconciliationCOBRA.html",EmailTemplates.EXECUTORRECONCILIATIONCOBRA.getEmail());

    }
}
