package asu.eng.gofund.controller.Payment;

import asu.eng.gofund.model.Donation;

import java.util.Map;

public class USD2EGPConverter extends CurrencyConverterDecorator{
    private final double exchangeRate = 50;

    public USD2EGPConverter(Donation decoratedDonation) {
        super(decoratedDonation);
    }

    @Override
    public boolean executePayment(IPaymentStrategy paymentStrategy, Map<String, String> paymentInfo, double amount) {
        if (paymentStrategy instanceof CreditCardPayment) {
            amount = amount * exchangeRate;
        } else if (paymentStrategy instanceof FawryPayment) {
            amount = amount * exchangeRate;
        }
        return decoratedDonation.executePayment(paymentStrategy, paymentInfo, amount);
    }
}
