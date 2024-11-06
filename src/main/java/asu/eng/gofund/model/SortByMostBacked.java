package asu.eng.gofund.model;

import java.util.List;
import java.util.stream.Collectors;

public class SortByMostBacked implements ICampaignSortingStrategy {
    @Override
    public List<Campaign> sort(List<Campaign> campaigns) {
        return campaigns.stream()
                .sorted((c1, c2) -> c2.getCurrentAmount().compareTo(c1.getCurrentAmount()))
                .collect(Collectors.toList());
    }
}