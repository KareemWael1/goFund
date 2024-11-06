// FilterByEndDate.java
package asu.eng.gofund.model.Filtering;

import asu.eng.gofund.model.Campaign;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FilterByEndDate implements ICampaignFilteringStrategy {
    private final Date endDate;

    public FilterByEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public List<Campaign> filter(List<Campaign> campaigns) {
        return campaigns.stream()
                .filter(campaign -> campaign.getEndDate().before(endDate))
                .collect(Collectors.toList());
    }
}