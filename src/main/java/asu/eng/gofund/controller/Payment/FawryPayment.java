package asu.eng.gofund.controller.Payment;

import java.util.Map;

public class FawryPayment implements IPaymentStrategy{

    @Override
    public boolean executePayment(Map<String, String> paymentInfo, double amount) {
        IPaymentAdapter paymentAdapter = new FawryPaymentAdapter();
        return paymentAdapter.executePayment(paymentInfo, amount);
    }
}
