package asu.eng.gofund.model.Filtering;

import asu.eng.gofund.model.Campaign;
import java.util.List;

public class CampaignFilterer {
    private ICampaignFilteringStrategy strategy;

    public CampaignFilterer(ICampaignFilteringStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Campaign> filterCampaigns(List<Campaign> campaigns) {
        return strategy.filter(campaigns);
    }

    public void setStrategy(ICampaignFilteringStrategy strategy) {
        this.strategy = strategy;
    }

}