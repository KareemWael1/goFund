package asu.eng.gofund.model;

import java.util.ArrayList;
import java.util.List;

public class MilestoneComposite implements MilestoneComponent {
    public final String name;
    public final List<MilestoneComponent> children = new ArrayList<>();

    public MilestoneComposite(String name) {
        this.name = name;
    }

    @Override
    public double getProgress() {
        return children.stream()
                .mapToDouble(MilestoneComponent::getProgress)
                .average()
                .orElse(0.0);
    }

    @Override
    public String displayDetails(String indentation) {
        StringBuilder details = new StringBuilder();
        details.append(indentation).append("Milestone: ").append(name).append("\n");
        for (MilestoneComponent child : children) {
            details.append(child.displayDetails(indentation + "    ")); // Add 4 spaces for indentation
        }
        return details.toString();
    }

    @Override
    public void add(MilestoneComponent component) {
        children.add(component);
    }

    @Override
    public void remove(MilestoneComponent component) {
        children.remove(component);
    }

    @Override
    public List<MilestoneComponent> getChildren() {
        return children;
    }
    public String getName() {
        return name;
    }
}
