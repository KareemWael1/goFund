package asu.eng.gofund.controller;

import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.repo.CampaignRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/campaign")
public class CampaignController {

    @Autowired
    private CampaignRepo campaignRepo;

    //Add a new campaign
    @PostMapping("")
    public ResponseEntity<Campaign> addCampaign(@RequestBody Campaign campaign) {
        Campaign savedCampaign = campaignRepo.save(campaign);
        return ResponseEntity.ok(savedCampaign);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable Long id, @RequestBody Campaign campaignDetails) {
        Optional<Campaign> optionalCampaign = campaignRepo.findById(id);
        if (optionalCampaign.isPresent()) {
            Campaign campaign = optionalCampaign.get();
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
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        if (campaignRepo.existsById(id)) {
            campaignRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campaign> getCampaign(@PathVariable Long id) {
        Optional<Campaign> campaign = campaignRepo.findById(id);
        return campaign.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("")
    public ResponseEntity<Iterable<Campaign>> getAllCampaigns() {
        Iterable<Campaign> campaigns = campaignRepo.findAll();
        return ResponseEntity.ok(campaigns);
    }

}
