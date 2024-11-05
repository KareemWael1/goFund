package asu.eng.gofund.controller;

import asu.eng.gofund.model.Campaign;

import java.util.List;

public interface ICampaignFilteringStrategy {
    List<Campaign> filter(List<Campaign> campaigns);
}
