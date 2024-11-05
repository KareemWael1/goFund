package asu.eng.gofund.controller;

import asu.eng.gofund.model.Campaign;

import java.util.List;
import java.util.stream.Collectors;

public class FilterByCategory implements ICampaignFilteringStrategy {
    private Long category;

    public FilterByCategory(Long category) {
        this.category = category;
    }

    @Override
    public List<Campaign> filter(List<Campaign> campaigns) {
        return campaigns.stream()
                .filter(campaign -> campaign.getCategory().getValue().equals(category))
                .collect(Collectors.toList());
    }
}
