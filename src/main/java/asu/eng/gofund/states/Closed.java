package asu.eng.gofund.states;

import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.CampaignStatus;

public class Closed implements ICampaignState{
    @Override
    public boolean donate(Campaign campaign, double amount) {
        return false;
    }

    @Override
    public boolean close(Campaign campaign) {
        return false;
    }

    @Override
    public boolean reopen(Campaign campaign) {
        campaign.setState(new Open());
        return true;
    }

    @Override
    public long getStateValue() {
        return CampaignStatus.CLOSED.getValue();
    }

    @Override
    public String getStateName() {
        return "Closed";
    }
}
