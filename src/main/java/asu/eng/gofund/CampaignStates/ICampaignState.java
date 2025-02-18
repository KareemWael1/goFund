package asu.eng.gofund.CampaignStates;

import asu.eng.gofund.model.Campaign;

public interface ICampaignState {
    boolean donate(Campaign campaign, double amount);
    boolean close(Campaign campaign);
    boolean reopen(Campaign campaign);
    long getStateValue();

    String getStateName();
}
