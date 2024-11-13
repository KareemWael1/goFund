package asu.eng.gofund.model.Sorting;

import asu.eng.gofund.model.Campaign;

import java.util.List;
import java.util.stream.Collectors;

public class SortByOldest implements ICampaignSortingStrategy {
    @Override
    public List<Campaign> sort(List<Campaign> campaigns) {
        return campaigns.stream()
                .sorted((c1, c2) -> c1.getStartDate().compareTo(c2.getStartDate()))
                .collect(Collectors.toList());
    }
}