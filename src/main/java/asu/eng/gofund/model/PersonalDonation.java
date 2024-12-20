package asu.eng.gofund.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "personalDonation")
public class PersonalDonation extends Donation{
    private Long campaignStarterId;

    public PersonalDonation() {
        super();
    }

    public PersonalDonation(Long id, Long donorId, double amount, Long campaignId, LocalDateTime donationDate, CustomCurrency currency, boolean isRefunded, String paymentStrategy, Long campaignStarterId) {
        super(id, donorId, amount, campaignId, donationDate, currency, isRefunded, paymentStrategy);
        this.campaignStarterId = campaignStarterId;
    }

    public PersonalDonation(Long donorId, double amount, Long campaignId, LocalDateTime donationDate, CustomCurrency currency, boolean isRefunded, String paymentStrategy, Long campaignStarterId) {
        super(donorId, amount, campaignId, donationDate, currency, isRefunded, paymentStrategy);
        this.campaignStarterId = campaignStarterId;
    }

    public Long getCampaignStarterId() {
        return campaignStarterId;
    }

    public void setCampaignStarterId(Long campaignStarterId) {
        this.campaignStarterId = campaignStarterId;
    }

    @Override
    public String toString() {
        return "PersonalDonation{" +
                "campaignStarterId=" + campaignStarterId +
                ", id=" + id +
                ", donorId=" + donorId +
                ", amount=" + amount +
                ", campaignId=" + campaignId +
                ", currency=" + currency +
                ", donationDate=" + donationDate +
                ", isRefunded=" + isRefunded +
                ", paymentStrategy='" + paymentStrategy + '\'' +
                '}';
    }
}
