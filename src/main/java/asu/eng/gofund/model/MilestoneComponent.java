package asu.eng.gofund.model;

import java.util.List;

public interface MilestoneComponent {
    double getProgress();

    String displayDetails(String indentation);

    void add(MilestoneComponent component);

    void remove(MilestoneComponent component);

    List<MilestoneComponent> getChildren();
}
