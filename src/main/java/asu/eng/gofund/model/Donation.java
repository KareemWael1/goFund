package asu.eng.gofund.model;

import asu.eng.gofund.controller.Payment.CreditCardPayment;
import asu.eng.gofund.controller.Payment.FawryPayment;
import asu.eng.gofund.controller.Payment.IPaymentStrategy;
import asu.eng.gofund.util.DatabaseUtil;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "donation_seq")
    protected Long id;
    protected Long donorId;
    protected double amount;
    protected Long campaignId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    protected LocalDateTime donationDate;
    protected boolean isRefunded;
    protected String paymentStrategy;

    public Donation() {
    }

    public Donation(Long id, Long donorId, double amount, Long campaignId, LocalDateTime donationDate, boolean isRefunded, String paymentStrategy) {
        this.id = id;
        this.donorId = donorId;
        this.amount = amount;
        this.campaignId = campaignId;
        this.donationDate = donationDate;
        this.isRefunded = isRefunded;
        this.paymentStrategy = paymentStrategy;
    }


    public Donation(Long donorId, double amount, Long campaignId, LocalDateTime donationDate, boolean isRefunded, String paymentStrategy) {
        this(null, donorId, amount, campaignId, donationDate, isRefunded, paymentStrategy);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public LocalDateTime getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(LocalDateTime donationDate) {
        this.donationDate = donationDate;
    }

    public boolean isRefunded() {
        return isRefunded;
    }

    public void setRefunded(boolean refunded) {
        isRefunded = refunded;
    }

    public String getpaymentStrategy(){
        return paymentStrategy;
    }

    public void setpaymentStrategy(String paymentStrategy){
        this.paymentStrategy = paymentStrategy;
    }

    public static IPaymentStrategy createPaymentStrategyFactory(String paymentStrategy){
        switch (paymentStrategy.toLowerCase().replaceAll(" ", "")){
            case "creditcard":
                return new CreditCardPayment();
            case "fawry":
                return new FawryPayment();
        }
        throw new IllegalArgumentException("Invalid payment strategy");
    }

    public static Donation createDonationFactory(String donationType, Long userId, double amount, Long campaignId, LocalDateTime donationDate, boolean isRefunded, String paymentStrategy, Long campaignStarterId, boolean regularDonation){
        switch (donationType.toLowerCase().replaceAll(" ", "")){
            case "personal":
                return new PersonalDonation(userId, amount, campaignId, donationDate, isRefunded, paymentStrategy, campaignStarterId);
            case "org":
                return new OrgDonation(userId, amount, campaignId, donationDate, isRefunded, paymentStrategy, regularDonation);
        }
        throw new IllegalArgumentException("Invalid payment strategy");
    }

    public boolean executePayment(IPaymentStrategy paymentStrategy, Map<String, String> paymentInfo, double amount){
        return paymentStrategy.executePayment(paymentInfo, amount);
    }

    @Override
    public String toString() {
        return "Donation{" +
                "id=" + id +
                ", donorId=" + donorId +
                ", amount=" + amount +
                ", campaignId=" + campaignId +
                ", donationDate=" + donationDate +
                ", isRefunded=" + isRefunded +
                ", paymentStrategy=" + paymentStrategy +
                '}';
    }
}
