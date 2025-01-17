package asu.eng.gofund.controller.Payment;

import java.util.Map;

public interface IPaymentAdapter {

    public boolean executePayment(Map<String, String> paymentInfo, double amount);
}
