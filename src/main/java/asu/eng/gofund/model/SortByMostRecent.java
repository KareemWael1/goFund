package asu.eng.gofund.model;

import java.util.List;
import java.util.stream.Collectors;

public class SortByMostRecent implements ICampaignSortingStrategy {
    @Override
    public List<Campaign> sort(List<Campaign> campaigns) {
        return campaigns.stream()
                .sorted((c1, c2) -> c2.getStartDate().compareTo(c1.getStartDate()))
                .collect(Collectors.toList());
    }
}