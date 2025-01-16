package asu.eng.gofund.controller.Payment;

import asu.eng.gofund.model.Donation;

import java.util.Map;

public class EGP2USDConverter extends CurrencyConverterDecorator{
    private final double exchangeRate = 0.02;

    public EGP2USDConverter(Donation decoratedDonation) {
        super(decoratedDonation);
    }

    @Override
    public boolean executePayment(IPaymentStrategy paymentStrategy, Map<String, String> paymentInfo, double amount) {
        if (paymentStrategy instanceof CreditCardPayment) {
            this.amount = amount * exchangeRate;
        } else if (paymentStrategy instanceof FawryPayment) {
            this.amount = amount * exchangeRate;
        }
        return decoratedDonation.executePayment(paymentStrategy, paymentInfo, this.amount);
    }

    @Override
    public void adjustAmount() {
        this.setAmount(this.decoratedDonation.getAmount() * exchangeRate);
    }
}
