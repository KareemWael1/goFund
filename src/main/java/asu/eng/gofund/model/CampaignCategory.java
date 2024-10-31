package asu.eng.gofund.model;

public enum CampaignCategory {
    EDUCATION,
    HEALTH,
    ENVIRONMENT,
    ANIMAL_WELFARE,
    HUMANITARIAN,
    OTHER;

    public Long getValue() {
        return (long) this.ordinal();
    }

    static public CampaignCategory getCategory(Long category) {
        return CampaignCategory.values()[category.intValue()];
    }
}
