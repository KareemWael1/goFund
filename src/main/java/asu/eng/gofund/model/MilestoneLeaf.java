package asu.eng.gofund.model;

import java.util.List;

public class MilestoneLeaf implements MilestoneComponent {
    public final String name;
    public final double targetAmount;
    public final double currentFunds;
    public final List<MilestoneComponent> children = null;

    public MilestoneLeaf(String name, double targetAmount, double currentFunds) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentFunds = currentFunds;
    }

    @Override
    public double getProgress() {
        return (currentFunds / targetAmount) * 100;
    }

    @Override
    public String displayDetails(String indentation) {
        return indentation + name + " Collected : " + currentFunds + " Out of " + targetAmount +  "\n";
    }

    @Override
    public void add(MilestoneComponent component) {
        throw new UnsupportedOperationException("Cannot add to a leaf milestone.");
    }

    @Override
    public void remove(MilestoneComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a leaf milestone.");
    }

    @Override
    public List<MilestoneComponent> getChildren() {
        throw new UnsupportedOperationException("Leaf milestones do not have children.");
    }
    public String getName() {
        return name;
    }
}

