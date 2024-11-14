package asu.eng.gofund.model;

import asu.eng.gofund.util.DatabaseUtil;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Donation<DateTime> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "donation_seq")
    protected Long id;
    protected Long donorId;
    protected double amount;
    protected Long campaignId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    protected LocalDateTime donationDate;
    protected boolean isRefunded;
    protected int status;

    public Donation() {
    }

    public Donation(Long id, Long donorId, double amount, Long campaignId, LocalDateTime donationDate, boolean isRefunded, int status) {
        this.id = id;
        this.donorId = donorId;
        this.amount = amount;
        this.campaignId = campaignId;
        this.donationDate = donationDate;
        this.isRefunded = isRefunded;
        this.status = status;
    }


    public Donation(Long donorId, double amount, Long campaignId, LocalDateTime donationDate, boolean isRefunded, int status) {
        this(null, donorId, amount, campaignId, donationDate, isRefunded, status);
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
                ", status=" + status +
                '}';
    }

    // DB operations
    public static List<Donation> getAllDonations() {
        String query = "SELECT * FROM donation WHERE is_refunded = false";
        return DatabaseUtil.getConnection().query(query, new BeanPropertyRowMapper<>(Donation.class));
    }

    public static List<Donation> getDonationsByCampaignId(Long campaignId) {
        String query = "SELECT * FROM donation WHERE campaign_id = ? AND is_refunded = false";
        return DatabaseUtil.getConnection().query(query, new BeanPropertyRowMapper<>(Donation.class), campaignId);
    }

    public static List<Donation> getDonationsByDonorId(Long donorId) {
        String query = "SELECT * FROM donation WHERE donor_id = ? AND is_refunded = false";
        return DatabaseUtil.getConnection().query(query, new BeanPropertyRowMapper<>(Donation.class), donorId);
    }

    public static List<Donation> getDonationsByStatus(int status) {
        String query = "SELECT * FROM donation WHERE status = ? AND is_refunded = false";
        return DatabaseUtil.getConnection().query(query, new BeanPropertyRowMapper<>(Donation.class), status);
    }

    public static List<Donation> getDonationsByDonorIdAndCampaignId(Long donorId, Long campaignId) {
        String query = "SELECT * FROM donation WHERE donor_id = ? AND campaign_id = ? AND is_refunded = false";
        return DatabaseUtil.getConnection().query(query, new BeanPropertyRowMapper<>(Donation.class), donorId, campaignId);
    }

    public static Donation getDonationById(Long id) {
        String query = "SELECT * FROM donation WHERE id = ? AND is_refunded = false";
        return DatabaseUtil.getConnection().queryForObject(query, new BeanPropertyRowMapper<>(Donation.class), id);
    }

    public static int addDonation(Donation donation) {
        String query = "INSERT INTO donation (donor_id, amount, campaign_id, donation_date, is_refunded, status) VALUES (?, ?, ?, ?, ?, ?)";
        return DatabaseUtil.getConnection().update(query, donation.getDonorId(), donation.getAmount(), donation.getCampaignId(), donation.getDonationDate(), donation.isRefunded(), donation.getStatus());
    }

    public int updateDonation() {
        String query = "UPDATE donation SET donor_id = ?, amount = ?, campaign_id = ?, donation_date = ?, is_refunded = ?, status = ? WHERE id = ?";
        return DatabaseUtil.getConnection().update(query, this.getDonorId(), this.getAmount(), this.getCampaignId(), this.getDonationDate(), this.isRefunded(), this.getStatus(), this.getId());
    }

    public static int refundDonation(Long id) {
        String query = "UPDATE donation SET is_refunded = true WHERE id = ?";
        return DatabaseUtil.getConnection().update(query, id);
    }
}
