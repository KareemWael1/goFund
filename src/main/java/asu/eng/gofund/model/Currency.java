package asu.eng.gofund.model;

public enum Currency {
    USD,
    EUR,
    GBP,
    JPY,
    EGP,
    INR;

    public Long getValue() {
        return (long) this.ordinal();
    }

    public static Currency getCurrency(Long currency) {
        return Currency.values()[currency.intValue()];
    }
}
