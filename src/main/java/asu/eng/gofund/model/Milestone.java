package asu.eng.gofund.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double targetAmount;

    @Column(nullable = false)
    private double currentFunds;

    @ManyToOne
    @JoinColumn(name = "parent_id") // Foreign key for self-referencing
    private Milestone parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Milestone> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false) // Foreign key to associate with a campaign
    private Campaign campaign;

    // Constructors
    public Milestone() {}

    public Milestone(String name, double targetAmount, double currentFunds, Campaign campaign, Milestone parent) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentFunds = currentFunds;
        this.campaign = campaign;
        this.parent = parent;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getCurrentFunds() {
        return currentFunds;
    }

    public void setCurrentFunds(double currentFunds) {
        this.currentFunds = currentFunds;
    }

    public Milestone getParent() {
        return parent;
    }

    public void setParent(Milestone parent) {
        this.parent = parent;
    }

    public List<Milestone> getChildren() {
        return children;
    }

    public void setChildren(List<Milestone> children) {
        this.children = children;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    // Utility Methods
    public void addChild(Milestone child) {
        child.setParent(this);
        children.add(child);
    }

    public void removeChild(Milestone child) {
        child.setParent(null);
        children.remove(child);
    }
}
