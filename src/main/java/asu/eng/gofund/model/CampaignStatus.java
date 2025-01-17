package asu.eng.gofund.model;

public enum CampaignStatus {
    OPEN,
    CLOSED,
    COMPLETED;

    public Long getValue() {
        return (long) this.ordinal();
    }

    public static CampaignStatus getStatus(Long status) {
        return CampaignStatus.values()[status.intValue()];
    }
}
