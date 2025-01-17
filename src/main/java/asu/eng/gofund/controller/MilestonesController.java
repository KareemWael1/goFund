package asu.eng.gofund.controller;

import asu.eng.gofund.model.MilestoneComponent;
import asu.eng.gofund.model.Milestone;
import asu.eng.gofund.model.MilestoneConverter;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.MilestonesRepo;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/milestones")
public class MilestonesController {
    @Autowired
    private MilestonesRepo milestonesRepo;
    @Autowired
    private CampaignRepo campaignRepo;

    @GetMapping("/{campaignId}")
    public String getMilestones(@PathVariable Long campaignId, Model model) {
        List<Milestone> rawMilestones = milestonesRepo.getAllByCampaignId(campaignId);
        List<MilestoneComponent> milestoneStructure = MilestoneConverter.convertToComposite(rawMilestones);

        model.addAttribute("milestoneDetails", milestoneStructure.get(0).displayDetails(""));
        model.addAttribute("campaignId", campaignId);
        model.addAttribute("allMilestones", rawMilestones); // For the parent dropdown

        return "milestones/view";
    }
    @PostMapping("/{campaignId}/addMilestone")
    public String addMilestone(
            @PathVariable Long campaignId,
            @RequestParam String name,
            @RequestParam(required = false) Double targetAmount, // Changed to Double
            @RequestParam String type,
            @RequestParam(required = false) Long parentId,
            Model model) {
        System.out.println("Adding milestone: " + name + " to campaign: " + campaignId);
        // Create the new milestone
        Milestone newMilestone = new Milestone();
        newMilestone.setName(name);
        newMilestone.setCampaign(campaignRepo.findCampaignByIdAndDeletedFalse(campaignId));
        newMilestone.setParent(parentId != null ? milestonesRepo.findMilestoneById(parentId) : null);
        if ("LEAF".equals(type)) {
            newMilestone.setCurrentFunds(0);
            newMilestone.setTargetAmount(targetAmount);
        } else {
            newMilestone.setTargetAmount(0);
        }

        // Save the new milestone
        milestonesRepo.save(newMilestone);


        // Redirect back to the milestones page
        return "redirect:/milestones/" + campaignId;
    }


}
