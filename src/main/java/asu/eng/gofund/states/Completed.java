package asu.eng.gofund.states;

import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.CampaignStatus;

public class Completed implements ICampaignState{
    @Override
    public boolean donate(Campaign campaign, double amount) {
        return false;
    }

    @Override
    public long getStateValue() {
        return CampaignStatus.COMPLETED.getValue();
    }

    @Override
    public String getStateName() {
        return "Completed";
    }
}
