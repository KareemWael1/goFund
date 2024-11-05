package asu.eng.gofund.model;

import asu.eng.gofund.util.DatabaseUtil;
import jakarta.persistence.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campaign_id_seq")
    private Long id;
    private boolean isDeleted;
    private String name;
    private String description;
    private String imageUrl;
    private Long status;
    private Long currency;
    private Long category;
    private Long starterId;
    @ManyToMany
    @JoinTable(name = "address_campaign",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private List<Address> addresses;
    private String bankAccountNumber;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Address location;
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public Campaign() {
    }

    public Campaign(Long id, String name, String description, String imageUrl,
                    CampaignStatus campaignStatus, Currency currency,
                    Long category, Long starterId, String bankAccountNumber, List<Address> addresses, List<Comment> comments, Address location) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = campaignStatus.getValue();
        this.currency = currency.getValue();
        this.category = category;
        this.starterId = starterId;
        this.bankAccountNumber = bankAccountNumber;
        this.addresses = addresses;
        this.location = location;
        this.comments = comments;
    }

    public Campaign(boolean isDeleted, String name, String description, String imageUrl,
                    CampaignStatus campaignStatus, Currency currency,
                    Long category, Long starterId, String bankAccountNumber, List<Address> addresses, List<Comment> comments, Address location) {
        this.isDeleted = isDeleted;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = campaignStatus.getValue();
        this.currency = currency.getValue();
        this.category = category;
        this.starterId = starterId;
        this.bankAccountNumber = bankAccountNumber;
        this.addresses = addresses;
        this.location = location;
        this.comments = comments;

    }

    public boolean refundDonation(Long donationDetailsId) {
        //TODO: Implement this method
        return false;
    }

    public boolean closeCampaign() {
        setStatus(CampaignStatus.CLOSED);
        return true;
    }

    boolean deleteCampaign(Long id) {
        setDeleted(true);
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CampaignStatus getStatus() {
        return CampaignStatus.getStatus(status);
    }

    public void setStatus(CampaignStatus status) {
        this.status = status.getValue();
    }

    public Currency getCurrency() {
        return Currency.getCurrency(currency);
    }

    public void setCurrency(Currency currency) {
        this.currency = currency.getValue();
    }

    public CampaignCategory getCategory() {
        return CampaignCategory.getCategory(category);
    }

    public void setCategory(CampaignCategory category) {
        this.category = category.getValue();
    }

    public Long getStarterId() {
        return starterId;
    }

    public void setStarterId(Long starterId) {
        this.starterId = starterId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean addDonor(Donor donor) {
        return false;
    }

    public List<Donor> listDonors() {
        return null;
    }

    public List<Comment> listComments() {
        return comments;
    }
}
