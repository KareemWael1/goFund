package asu.eng.gofund.controller.Payment;

import java.util.Map;

public class FawryPayment implements IPaymentStrategy{
    private String fawryCode;
    private String fawryAccount;
    private String fawryPassword;

    @Override
    public boolean executePayment(Map<String, String> paymentInfo, double amount) {
        fawryCode = paymentInfo.get("fawryCode");
        fawryAccount = paymentInfo.get("fawryAccount");
        fawryPassword = paymentInfo.get("fawryPassword");

        if (amount <= 0) {
            System.out.println("Invalid payment amount");
            return false;
        }

        // Payment processing logic
        System.out.println("Fawry Payment Successful");
        return true;
    }
}
