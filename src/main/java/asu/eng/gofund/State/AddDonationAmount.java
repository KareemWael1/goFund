package asu.eng.gofund.State;

import asu.eng.gofund.model.Donation;

public class AddDonationAmount implements IDonationState {

    @Override
    public void NextState(Donation donation) {

        donation.setState(new DonationClosed());
    }

    @Override
    public void PrevState(Donation donation) {

    }

    @Override
    public void ExecuteState(Donation donation) {

    }

}
