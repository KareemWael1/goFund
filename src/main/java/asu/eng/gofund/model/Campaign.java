package asu.eng.gofund.model;


import jakarta.persistence.*;


import java.util.*;

@Entity
public class Campaign implements Subject, IIterator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean deleted = false;
    private String name;
    private String description;
    private String imageUrl;
    private Long status = 0L;
    private Long currency = 0L;
    private Long category = 0L;
    private Long starterId;
    private Date startDate = new Date();
    private Date endDate;
    protected double currentAmount = 0;
    private double targetAmount;
    @ManyToMany
    @JoinTable(name = "address_campaign",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private List<Address> addresses;
    private String bankAccountNumber;
    @ManyToMany
    @JoinTable(
            name = "subject_observer",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "observer_id")
    )
    private final Set<User> observers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "starterId", insertable = false, updatable = false)
    private User user;
    @Transient
    private static final ArrayList<String> logs = new ArrayList<>();

    public Campaign() {
        super();
    }

    public Campaign(String name, String description) {
        this.name = name;
        this.description = description;

    }

    public Campaign(Long id, String name, String description, String imageUrl,
                    CampaignStatus campaignStatus, CustomCurrency currency,
                    Long category, Long starterId, String bankAccountNumber, Date startDate,
                    Date endDate, Long currentAmount, List<Address> addresses, double targetAmount) {

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
        this.startDate = startDate;
        this.endDate = endDate;
        this.currentAmount = currentAmount;
        this.targetAmount = targetAmount;
    }

    public Campaign(boolean isDeleted, String name, String description, String imageUrl,
                    CampaignStatus campaignStatus, CustomCurrency currency,
                    Long category, Long starterId, String bankAccountNumber,
                    Date startDate, Date endDate, Long currentAmount, List<Address> addresses,double targetAmount) {
        this.deleted = isDeleted;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = campaignStatus.getValue();
        this.currency = currency.getValue();
        this.category = category;
        this.starterId = starterId;
        this.bankAccountNumber = bankAccountNumber;
        this.addresses = addresses;
        this.startDate = startDate;
        this.endDate = endDate;
        this.currentAmount = currentAmount;
        this.targetAmount = targetAmount;

    }
    public Set<User> getObservers() {
        return observers;
    }
    public void donate(double amount) {
        this.currentAmount += amount;
        notifyObservers();
        logs.add("Donation of " + amount + " was made at " + new Date());
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add((User) observer);
        logs.add("User " + ((User) observer).getUsername() + " has registered as an observer at " + new Date());
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove((User) observer);
        logs.add("User " + ((User) observer).getUsername() + " has removed as an observer at " + new Date());
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this.getName(), this.getCurrentAmount());
        }
    }
    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Long currentAmount) {
        this.currentAmount = currentAmount;
    }

    public boolean refundDonation(Long donationDetailsId) {
        //TODO: Implement this method
        return false;
    }

    public boolean closeCampaign() {
        setStatus(CampaignStatus.CLOSED);
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

    public CustomCurrency getCurrency() {
        return CustomCurrency.getCurrency(currency);
    }

    public void setCurrency(CustomCurrency currency) {
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
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Iterator<String> createLogsIterator() {
        return logs.iterator();
    }

}
