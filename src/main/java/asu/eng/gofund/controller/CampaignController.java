package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.*;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.UserRepo;
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

    //Add a new campaign
    @PostMapping("")
    public ResponseEntity<Campaign> addCampaign(@RequestBody Campaign campaign) {
        Campaign savedCampaign = campaignRepo.save(campaign);
        return ResponseEntity.ok(savedCampaign);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable Long id, @RequestBody Campaign campaignDetails) {
        try {
            Campaign campaign = campaignRepo.getCampaignsByIdAndDeletedFalse(id);
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
        Campaign campaign = campaignRepo.getCampaignsByIdAndDeletedFalse(id);
        if (Objects.equals(campaign.getStarterId(), user.getId()) || Objects.equals(user.getRole(), "admin")) {
            campaign.setDeleted(true);
            campaignRepo.save(campaign);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Campaign> getCampaign(@PathVariable Long id) {
        Optional<Campaign> campaign = campaignRepo.findById(id);
        return campaign.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("")
    public ResponseEntity<Iterable<Campaign>> getAllCampaigns(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) CampaignCategory filterCategory,
            @RequestParam(required = false) String filterTitle,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date filterEndDate) {

        List<Campaign> campaigns = campaignRepo.findAllByDeletedFalse();


        if (filterCategory != null) {
            ICampaignFilteringStrategy filteringStrategy = new FilterByCategory(filterCategory);
            campaigns = filteringStrategy.filter(campaigns);
        }
        if (filterTitle != null) {
            ICampaignFilteringStrategy filteringStrategy = new FilterByTitleContains(filterTitle);
            campaigns = filteringStrategy.filter(campaigns);
        }
        if (filterEndDate != null) {
            ICampaignFilteringStrategy filteringStrategy = new FilterByEndDate(filterEndDate);
            campaigns = filteringStrategy.filter(campaigns);
        }


        if ("mostRecent".equalsIgnoreCase(sort)) {
            ICampaignSortingStrategy sortingStrategy = new SortByMostRecent();
            campaigns = sortingStrategy.sort(campaigns);
        } else if ("oldest".equalsIgnoreCase(sort)) {
            ICampaignSortingStrategy sortingStrategy = new SortByOldest();
            campaigns = sortingStrategy.sort(campaigns);
        } else if ("mostBacked".equalsIgnoreCase(sort)) {
            ICampaignSortingStrategy sortingStrategy = new SortByMostBacked();
            campaigns = sortingStrategy.sort(campaigns);
        }

        return ResponseEntity.ok(campaigns);
    }
    @PutMapping("/{campaignId}/donate")
    public ResponseEntity<String> donateToCampaign(
            @PathVariable Long campaignId,
            @CurrentUser User user,
            @RequestParam double amount) {

        try {
            // Retrieve the campaign and user from the repositories
            Campaign campaign = campaignRepo.getCampaignsByIdAndDeletedFalse(campaignId);
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
            Campaign campaign = campaignRepo.getCampaignsByIdAndDeletedFalse(campaignId);

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
            Campaign campaign = campaignRepo.getCampaignsByIdAndDeletedFalse(campaignId);

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

}
