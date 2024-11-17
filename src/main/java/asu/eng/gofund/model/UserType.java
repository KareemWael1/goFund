package asu.eng.gofund.model;

public enum UserType {
    Admin(0),
    CampaignCreator(1),
    Basic(2);

    private final int value;

    UserType(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }

    public static UserType getUserType(Long userType) {
        return UserType.values()[userType.intValue()];
    }
}
