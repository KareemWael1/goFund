package asu.eng.gofund.State;

import asu.eng.gofund.controller.Payment.IPaymentStrategy;
import asu.eng.gofund.model.Donation;


public class PerformDonation implements IDonationState {
    @Override
    public void NextState(Donation donation) {


        donation.setState( new SyncDonation());
    }

    @Override
    public void PrevState(Donation donation) {

    }

    @Override
    public void ExecuteState(Donation donation) {

        IPaymentStrategy strategy;
        strategy = Donation.createPaymentStrategyFactory(donation.getPaymentStrategy());
        donation.executePayment(strategy, donation.getCredentials(), donation.getAmount());
    }
}
