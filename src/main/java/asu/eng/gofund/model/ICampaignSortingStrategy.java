package asu.eng.gofund.model;
import java.util.List;


public interface ICampaignSortingStrategy {
    List<Campaign> sort(List<Campaign> campaigns);
}
