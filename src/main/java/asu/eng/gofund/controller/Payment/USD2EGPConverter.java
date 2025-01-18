package asu.eng.gofund.controller.Payment;

import asu.eng.gofund.model.Donation;

import java.util.Map;

public class USD2EGPConverter extends CurrencyConverterDecorator{
    private final double exchangeRate = Double.parseDouble(System.getenv("USD_TO_EGP"));

    public USD2EGPConverter(Donation decoratedDonation) {
        super(decoratedDonation);
    }

    @Override
    public boolean executePayment(IPaymentStrategy paymentStrategy, Map<String, String> paymentInfo, double amount) {
        this.decoratedDonation.setAmount(amount * exchangeRate);
        return decoratedDonation.executePayment(paymentStrategy, paymentInfo, this.decoratedDonation.getAmount());
    }
}
