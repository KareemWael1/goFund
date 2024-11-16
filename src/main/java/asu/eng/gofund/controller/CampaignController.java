package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.*;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.UserRepo;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/campaign")
public class CampaignController {

    @Autowired
    private CampaignRepo campaignRepo;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("")
    public ResponseEntity<Campaign> addCampaign(@RequestBody Campaign campaign) {
        Campaign savedCampaign = campaignRepo.save(campaign);
        return ResponseEntity.ok(savedCampaign);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable Long id, @RequestBody Campaign campaignDetails) {
        try {
            Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(id);
            campaign.setName(campaignDetails.getName());
            campaign.setDescription(campaignDetails.getDescription());
            campaign.setImageUrl(campaignDetails.getImageUrl());
            campaign.setStatus(campaignDetails.getStatus());
            campaign.setCurrency(campaignDetails.getCurrency());
            campaign.setCategory(campaignDetails.getCategory());
            campaign.setStarterId(campaignDetails.getStarterId());
            campaign.setBankAccountNumber(campaignDetails.getBankAccountNumber());
            campaign.setAddresses(campaignDetails.getAddresses());
            Campaign updatedCampaign = campaignRepo.save(campaign);
            return ResponseEntity.ok(updatedCampaign);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id, @CurrentUser User user) {
        Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(id);
        if (Objects.equals(campaign.getStarterId(), user.getId()) || Objects.equals(user.getRole(), "admin")) {
            campaign.setDeleted(true);
            campaignRepo.save(campaign);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Campaign> getCampaign(@PathVariable Long id) {
        Optional<Campaign> campaign = Optional.ofNullable(campaignRepo.findCampaignByIdAndDeletedFalse(id));
        return campaign.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/list")
    public String getAllCampaigns(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) CampaignCategory filterCategory,
            @RequestParam(required = false) String filterTitle,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date filterEndDate,
            Model model) {

        List<Campaign> campaigns;

        if (filterCategory != null || filterTitle != null || filterEndDate != null) {
            campaigns = campaignRepo.findByFilters(filterCategory, filterTitle, filterEndDate);
        } else {
            campaigns = campaignRepo.findAll();
        }

        if ("mostRecent".equalsIgnoreCase(sort)) {
            campaigns = campaignRepo.findAllOrderByMostRecent();
        } else if ("oldest".equalsIgnoreCase(sort)) {
            campaigns = campaignRepo.findAllOrderByOldest();
        } else if ("mostBacked".equalsIgnoreCase(sort)) {
            campaigns = campaignRepo.findAllOrderByMostBacked();
        }

        model.addAttribute("campaigns", campaigns);
        return "homePage";
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<Campaign> campaigns = campaignRepo.findAllByDeletedFalse();
        model.addAttribute("campaigns", campaigns);
        return "homePage";
    }
    @PutMapping("/{campaignId}/donate")
    public ResponseEntity<String> donateToCampaign(
            @PathVariable Long campaignId,
            @CurrentUser User user,
            @RequestParam double amount) {

        try {
            // Retrieve the campaign and user from the repositories
            Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(campaignId);
            // make operations on user if needed
            campaign.donate(amount);
            campaignRepo.save(campaign);
            return ResponseEntity.ok("Donation successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Donation failed "+ e.getMessage());
        }

    }

    @PostMapping("/{campaignId}/subscribe/{userId}")
    public ResponseEntity<String> subscribeToCampaign(
            @PathVariable Long campaignId,
            @PathVariable Long userId) {

        try {
            // Retrieve the campaign and user from the repositories
            Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(campaignId);

            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Add the user as an observer of the campaign
            user.subscribe(campaign);

            // Save the updated campaign if needed
            campaignRepo.save(campaign);

            return ResponseEntity.ok("User subscribed to campaign successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Subscription failed " + e.getMessage());
        }
    }

    @PostMapping("/{campaignId}/unsubscribe/{userId}")
    public ResponseEntity<String> unsubscribeFromCampaign(
            @PathVariable Long campaignId,
            @PathVariable Long userId) {

        try {
            // Retrieve the campaign and user from the repositories
            Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(campaignId);

            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Remove the user as an observer of the campaign
            user.unsubscribe(campaign);

            // Save the updated campaign if needed
            campaignRepo.save(campaign);

            return ResponseEntity.ok("User unsubscribed from campaign successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unsubscription failed " + e.getMessage());
        }
    }

    @GetMapping("/campaigns/create")
    public String createCampaignPage() {
        return "createCampaignPage";
    }
}
