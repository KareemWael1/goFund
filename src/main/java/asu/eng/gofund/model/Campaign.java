package asu.eng.gofund.model;

import asu.eng.gofund.CampaignStates.Closed;
import asu.eng.gofund.CampaignStates.Completed;
import asu.eng.gofund.CampaignStates.ICampaignState;
import asu.eng.gofund.CampaignStates.Open;
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
    @ManyToOne
    @JoinColumn(name = "campaign_category_id")
    private CampaignCategory category;
    private Long starterId;
    private Date startDate = new Date();
    private Date endDate;
    protected double currentAmount = 0;
    private double targetAmount;
    @ManyToMany
    @JoinTable(name = "address_campaign", joinColumns = @JoinColumn(name = "campaign_id"), inverseJoinColumns = @JoinColumn(name = "address_id"))
    private List<Address> addresses;
    private String bankAccountNumber;
    @ManyToMany
    @JoinTable(name = "subject_observer", joinColumns = @JoinColumn(name = "subject_id"), inverseJoinColumns = @JoinColumn(name = "observer_id"))
    private final Set<User> observers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "starterId", insertable = false, updatable = false)
    private User user;
    @Transient
    private static final ArrayList<String> logs = new ArrayList<>();
    @Transient
    private ICampaignState state;

    public Campaign() {
        super();
        state = new Open();
    }

    public Campaign(String name, String description) {
        this.name = name;
        this.description = description;
        state = new Open();
    }

    public Campaign(Long id, String name, String description, String imageUrl,
            CampaignStatus campaignStatus, CustomCurrency currency,
            CampaignCategory category, Long starterId, String bankAccountNumber, Date startDate,
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
        state = new Open();
    }

    public Campaign(boolean isDeleted, String name, String description, String imageUrl,
            CampaignStatus campaignStatus, CustomCurrency currency,
            CampaignCategory category, Long starterId, String bankAccountNumber,
            Date startDate, Date endDate, Long currentAmount, List<Address> addresses, double targetAmount) {
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
        state = new Open();
    }

    @PostLoad
    private void initializeState() {
        if (status == null) {
            state = new Open();
            status = state.getStateValue();
            return;
        }
        if (status == 0) {
            state = new Open();
        } else if (status == 1) {
            state = new Closed();
        } else if (status == 2) {
            state = new Completed();
        } else {
            state = new Open();
        }
    }

    public Set<User> getObservers() {
        return observers;
    }



    public void donate(double amount) {
        if(this.state.donate(this, amount)){
            System.out.println("Donation successful");
            System.out.println(this.state.getStateName());
        }
        else{
            throw new IllegalStateException("Cannot donate to campaign in " + state.getStateName() + " state");
        }
    }

    public boolean closeCampaign() {
        this.state.close(this);
        return true;
    }


    public boolean reopenCampaign() {
        this.state.reopen(this);
        return true;
    }

    public void refundDonation(double amount) {
        this.currentAmount -= amount;
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

    @Override
    public Iterator<String> createLogsIterator() {
        return logs.iterator();
    }

}
