package asu.eng.gofund.State;

import asu.eng.gofund.model.Donation;

public interface IDonationState {
    public void NextState(Donation donation);
    public void PrevState(Donation donation);
}
