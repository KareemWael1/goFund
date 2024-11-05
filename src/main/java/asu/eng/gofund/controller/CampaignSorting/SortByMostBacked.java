package asu.eng.gofund.sorting;

import asu.eng.gofund.model.Campaign;
import java.util.List;
import java.util.Collections;

public class SortByMostBacked implements ICampaignSortingStrategy {
    @Override
    public List<Campaign> sort(List<Campaign> campaigns) {
        Collections.sort(campaigns, (c1, c2) -> c2.getComments().size() - c1.getComments().size());
        return campaigns;
    }
}
