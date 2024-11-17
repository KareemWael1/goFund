package asu.eng.gofund.model;

public enum CustomCurrency {
    USD,
    EGP;

    public Long getValue() {
        return (long) this.ordinal();
    }

    public static CustomCurrency getCurrency(Long currency) {
        return CustomCurrency.values()[currency.intValue()];
    }
}
