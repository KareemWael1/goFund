package asu.eng.gofund.view;

public class CampaignView {
    public String showCampaigns() {
        return "campaign";
    }

    public String showCampaignDetails() {
        return "campaignDetails";
    }

    public String showCreateCampaign() {
        return "createCampaign";
    }

    public String redirectToCampaign() {
        return "redirect:/campaign";
    }

    public String redirectToCampaignWithID(Long campaignID) {
        return "redirect:/campaign/" + campaignID;
    }
}