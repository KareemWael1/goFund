package asu.eng.gofund.controller.Payment;

import asu.eng.gofund.model.Donation;

import java.util.Map;

public abstract class CurrencyConverterDecorator extends Donation {
    protected Donation decoratedDonation;

    public CurrencyConverterDecorator(Donation decoratedDonation) {
        this.decoratedDonation = decoratedDonation;
    }

    public abstract boolean executePayment(IPaymentStrategy paymentStrategy, Map<String, String> paymentInfo, double amount);

    public abstract void adjustAmount();
}
