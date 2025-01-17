package asu.eng.gofund.CampaignStates;

import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.CampaignStatus;

public class Open implements ICampaignState{
    @Override
    public boolean donate(Campaign campaign, double amount) {
        if (amount > 0){
            campaign.setCurrentAmount(campaign.getCurrentAmount() + amount);
            campaign.notifyObservers();
            if (campaign.getCurrentAmount() < campaign.getTargetAmount())return true;
            campaign.setState(new Completed());
            return true;
        }
        return false;
    }

    @Override
    public boolean close(Campaign campaign) {
        campaign.setState(new Closed());
        return false;
    }

    @Override
    public boolean reopen(Campaign campaign) {
        return false;
    }


    @Override
    public long getStateValue() {
        return CampaignStatus.OPEN.getValue();
    }

    @Override
    public String getStateName() {
        return "Open";
    }
}
