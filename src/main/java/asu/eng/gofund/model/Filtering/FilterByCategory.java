package asu.eng.gofund.model.Filtering;

import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.CampaignCategory;

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
                .filter(campaign -> campaign.getCategory().getName().equals(category.getName()))
                .collect(Collectors.toList());
    }
}