package asu.eng.gofund.controller;

import asu.eng.gofund.model.Campaign;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FilterByEndDate implements ICampaignFilteringStrategy {
    private Date endDate;

    public FilterByEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public List<Campaign> filter(List<Campaign> campaigns) {
        return campaigns.stream()
                .filter(campaign -> campaign.getEndDate().before(endDate))
                .collect(Collectors.toList());
    }
}
