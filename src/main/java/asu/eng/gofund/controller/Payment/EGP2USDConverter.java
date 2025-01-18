package asu.eng.gofund.controller.Payment;

import asu.eng.gofund.model.Donation;

import java.util.Map;

public class EGP2USDConverter extends CurrencyConverterDecorator{
    private final double exchangeRate = Double.parseDouble(System.getenv("EGP_TO_USD"));

    public EGP2USDConverter(Donation decoratedDonation) {
        super(decoratedDonation);
    }

    @Override
    public boolean executePayment(IPaymentStrategy paymentStrategy, Map<String, String> paymentInfo, double amount) {
        this.decoratedDonation.setAmount(amount * exchangeRate);
        return decoratedDonation.executePayment(paymentStrategy, paymentInfo, this.decoratedDonation.getAmount());
    }
}
