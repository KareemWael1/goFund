package asu.eng.gofund.model;

public interface Observer {
    void update(Long CampaignId, Double reachedAmount);
}
