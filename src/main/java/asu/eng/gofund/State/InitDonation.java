package asu.eng.gofund.State;

import asu.eng.gofund.model.Donation;

public class InitDonation implements IDonationState {
    @Override
    public void NextState(Donation donation) {

        donation.setState(new AddDonationAmount());
    }

    @Override
    public void PrevState(Donation donation) {

    }

    @Override
    public void ExecuteState(Donation donation) {

    }
}
