package asu.eng.gofund.model;

import asu.eng.gofund.State.IDonationState;
import asu.eng.gofund.State.PerformDonation;
import asu.eng.gofund.controller.Payment.CreditCardPayment;
import asu.eng.gofund.controller.Payment.FawryPayment;
import asu.eng.gofund.controller.Payment.IPaymentStrategy;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
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
    protected CustomCurrency currency;
    protected boolean isRefunded;
    protected String paymentStrategy;

    @Transient
    private Map<String, String> credentials;

    @Transient
    public IDonationState ref;

    public Donation() {
        this.ref = new PerformDonation();
    }

    public Donation(Long id, Long donorId, double amount, Long campaignId, LocalDateTime donationDate, CustomCurrency currency, boolean isRefunded, String paymentStrategy) {
        this.id = id;
        this.donorId = donorId;
        this.amount = amount;
        this.campaignId = campaignId;
        this.donationDate = donationDate;
        this.currency = currency;
        this.isRefunded = isRefunded;
        this.paymentStrategy = paymentStrategy;
        this.ref = new PerformDonation();
    }


    public Donation(Long donorId, double amount, Long campaignId, LocalDateTime donationDate, CustomCurrency currency, boolean isRefunded, String paymentStrategy) {
        this(null, donorId, amount, campaignId, donationDate, currency, isRefunded, paymentStrategy);
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

    public void setCredentials(Map<String, String> credentials){
        this.credentials = credentials;
    }

    public Map<String, String> getCredentials(){
        return credentials;
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

    public CustomCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(CustomCurrency currency) {
        this.currency = currency;
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

    public static Donation createDonationFactory(String donationType, Long userId, double amount, Long campaignId, LocalDateTime donationDate, CustomCurrency currency, boolean isRefunded, String paymentStrategy, Long campaignStarterId, boolean regularDonation){
        switch (donationType.toLowerCase().replaceAll(" ", "")){
            case "personal":
                return new PersonalDonation(userId, amount, campaignId, donationDate, currency, isRefunded, paymentStrategy, campaignStarterId);
            case "org":
                return new OrgDonation(userId, amount, campaignId, donationDate, currency, isRefunded, paymentStrategy, regularDonation);
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
                ", currency=" + currency +
                ", isRefunded=" + isRefunded +
                ", paymentStrategy=" + paymentStrategy +
                '}';
    }

    public void setState(IDonationState state){
        this.ref = state;
    }

    public void executeNextState(){
        ref.NextState(this);
    }

    public void executePrevState(){
        ref.PrevState(this);
    }

    public void executeCurrentState(){
        ref.ExecuteState(this);
    }

}
