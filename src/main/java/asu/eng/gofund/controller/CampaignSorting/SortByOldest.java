package asu.eng.gofund.sorting;

import asu.eng.gofund.model.Campaign;
import java.util.List;
import java.util.Collections;

public class SortByOldest implements ICampaignSortingStrategy {
    @Override
    public List<Campaign> sort(List<Campaign> campaigns) {
        Collections.sort(campaigns, (c1, c2) -> c1.getId().compareTo(c2.getId()));
        return campaigns;
    }
}
