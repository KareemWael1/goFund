// FilterByTitleContains.java
package asu.eng.gofund.model;

import java.util.List;
import java.util.stream.Collectors;

public class FilterByTitleContains implements ICampaignFilteringStrategy {
    private final String keyword;

    public FilterByTitleContains(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public List<Campaign> filter(List<Campaign> campaigns) {
        return campaigns.stream()
                .filter(campaign -> campaign.getName().contains(keyword))
                .collect(Collectors.toList());
    }
}