package asu.eng.gofund.model.Sorting;

import asu.eng.gofund.model.Campaign;
import java.util.List;

public class CampaignSorter {
    private ICampaignSortingStrategy strategy;

    public CampaignSorter(ICampaignSortingStrategy strategy) {
        this.strategy = strategy;
    }

    //method for calling sort campaign from strategy
    public List<Campaign> sortCampaigns(List<Campaign> campaigns) {
        return strategy.sort(campaigns);
    }


    public void setStrategy(ICampaignSortingStrategy strategy) {
        this.strategy = strategy;
    }

}