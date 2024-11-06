package asu.eng.gofund.model;
import java.util.List;

public interface ICampaignFilteringStrategy {
    List<Campaign> filter(List<Campaign> campaigns);
}
