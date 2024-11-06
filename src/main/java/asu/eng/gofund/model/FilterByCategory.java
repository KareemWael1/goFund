package asu.eng.gofund.model;

import java.util.List;
import java.util.stream.Collectors;

public class FilterByCategory implements ICampaignFilteringStrategy {
    private final CampaignCategory category;

    public FilterByCategory(CampaignCategory category) {
        this.category = category;
    }

    @Override
    public List<Campaign> filter(List<Campaign> campaigns) {
        return campaigns.stream()
                .filter(campaign -> campaign.getCategory().equals(category))
                .collect(Collectors.toList());
    }
}