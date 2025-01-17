package asu.eng.gofund.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MilestoneConverter {

    public static List<MilestoneComponent> convertToComposite(List<Milestone> rawMilestones) {
        // Create a map to store all milestones by their ID for easy lookup
        Map<Long, Milestone> milestoneMap = new HashMap<>();
        // List to store root level components
        List<MilestoneComponent> rootComponents = new ArrayList<>();

        // First pass: populate the map
        for (Milestone milestone : rawMilestones) {
            milestoneMap.put(milestone.getId(), milestone);
        }

        // Second pass: create composite structure
        for (Milestone milestone : rawMilestones) {
            if (milestone.getParent() == null) {
                // This is a root milestone
                rootComponents.add(buildCompositeTree(milestone, milestoneMap));
            }
        }

        return rootComponents;
    }

    private static MilestoneComponent buildCompositeTree(Milestone milestone, Map<Long, Milestone> milestoneMap) {
        if (milestone.getChildren().isEmpty()) {
            // This is a leaf node
            return new MilestoneLeaf(
                    milestone.getName(),
                    milestone.getTargetAmount(),
                    milestone.getCurrentFunds()
            );
        } else {
            // This is a composite node
            MilestoneComposite composite = new MilestoneComposite(milestone.getName());

            // Recursively build the tree for each child
            for (Milestone child : milestone.getChildren()) {
                composite.add(buildCompositeTree(child, milestoneMap));
            }

            return composite;
        }
    }
}