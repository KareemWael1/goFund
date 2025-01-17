package asu.eng.gofund.controller.Payment;

import java.util.Map;

public class CreditCardPayment implements IPaymentStrategy{

    @Override
    public boolean executePayment(Map<String, String> paymentInfo, double amount) {
        IPaymentAdapter paymentAdapter = new StripePaymentAdapter();
        return paymentAdapter.executePayment(paymentInfo, amount);
    }
}
