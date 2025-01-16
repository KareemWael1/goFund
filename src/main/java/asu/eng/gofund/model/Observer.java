package asu.eng.gofund.model;

public interface Observer {
    void update(String campaignName, Double reachedAmount);
}
