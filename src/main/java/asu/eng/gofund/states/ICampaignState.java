package asu.eng.gofund.states;

import asu.eng.gofund.model.Campaign;

public interface ICampaignState {
    boolean donate(Campaign campaign, double amount);
    long getStateValue();
    String getStateName();
}
