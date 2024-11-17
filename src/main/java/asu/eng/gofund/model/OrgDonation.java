package asu.eng.gofund.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "orgDonation")
public class OrgDonation extends Donation{
    private boolean regularDonation;

    public OrgDonation() {
        super();
    }

    public OrgDonation(Long id, Long donorId, double amount, Long campaignId, LocalDateTime donationDate, CustomCurrency currency, boolean isRefunded, String paymentStrategy, boolean regularDonation) {
        super(id, donorId, amount, campaignId, donationDate, currency, isRefunded, paymentStrategy);
        this.regularDonation = regularDonation;
    }

    public OrgDonation(Long donorId, double amount, Long campaignId, LocalDateTime donationDate, CustomCurrency currency, boolean isRefunded, String paymentStrategy, boolean regularDonation) {
        super(donorId, amount, campaignId, donationDate, currency, isRefunded, paymentStrategy);
        this.regularDonation = regularDonation;
    }

    public boolean isRegularDonation() {
        return regularDonation;
    }

    public void setRegularDonation(boolean regularDonation) {
        this.regularDonation = regularDonation;
    }

    @Override
    public String toString() {
        return "OrgDonation{" +
                "regularDonation=" + regularDonation +
                ", id=" + id +
                ", donorId=" + donorId +
                ", amount=" + amount +
                ", campaignId=" + campaignId +
                ", donationDate=" + donationDate +
                ", currency=" + currency +
                ", isRefunded=" + isRefunded +
                ", paymentStrategy='" + paymentStrategy + '\'' +
                '}';
    }
}
