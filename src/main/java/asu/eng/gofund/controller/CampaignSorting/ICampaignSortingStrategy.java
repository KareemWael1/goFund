package asu.eng.gofund.sorting;

import asu.eng.gofund.model.Campaign;
import java.util.List;

public interface ICampaignSortingStrategy {
    List<Campaign> sort(List<Campaign> campaigns);
}
