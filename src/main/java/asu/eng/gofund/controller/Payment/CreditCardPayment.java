package asu.eng.gofund.controller.Payment;

import java.util.Map;

public class CreditCardPayment implements IPaymentStrategy{
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String cardHolderName;

    @Override
    public boolean executePayment(Map<String, String> paymentInfo, double amount) {
        cardNumber = paymentInfo.get("cardNumber");
        expiryDate = paymentInfo.get("expirationDate");
        cvv = paymentInfo.get("cvv");
        cardHolderName = paymentInfo.get("nameOnCard");

        return true;
    }
}
