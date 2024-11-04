package asu.eng.gofund.controller;

import asu.eng.gofund.controller.Payment.IPaymentStrategy;
import asu.eng.gofund.model.Donation;

import java.util.Map;

public class DonationController {
    private IPaymentStrategy paymentStrategy;

    public DonationController() {
    }

    public int createDonation(Donation donation) {
        return Donation.addDonation(donation);
    }

    public int updateDonation(Donation donation) {
        return donation.updateDonation();
    }

    public int refundDonation(Long id) {
        return Donation.refundDonation(id);
    }

    public Donation getDonation(Long id) {
        return Donation.getDonationById(id);
    }

    public void setPaymentStrategy(IPaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public boolean executePayment(Map<String, String> paymentInfo, double amount) {
        return paymentStrategy.executePayment(paymentInfo, amount);
    }

}
