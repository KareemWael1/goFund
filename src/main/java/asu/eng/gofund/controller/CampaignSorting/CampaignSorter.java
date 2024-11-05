package asu.eng.gofund.sorting;

import asu.eng.gofund.model.Campaign;
import java.util.List;

public class CampaignSorter {
    private ICampaignSortingStrategy strategy;

    public void setStrategy(ICampaignSortingStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Campaign> sortCampaigns(List<Campaign> campaigns) {
        return strategy.sort(campaigns);
    }
}
