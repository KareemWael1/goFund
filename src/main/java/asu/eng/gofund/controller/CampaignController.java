package asu.eng.gofund.controller;

import asu.eng.gofund.model.*;
import asu.eng.gofund.model.Filtering.*;
import asu.eng.gofund.model.Sorting.*;
import asu.eng.gofund.repo.CampaignRepo;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/campaign")
public class CampaignController {

    @Autowired
    private CampaignRepo campaignRepo;

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

//    @GetMapping("")
//    public ResponseEntity<Iterable<Campaign>> getAllCampaigns() {
//        Iterable<Campaign> campaigns = campaignRepo.findAll();
//        return ResponseEntity.ok(campaigns);
//    }

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
        List<Campaign> campaigns = campaignRepo.findAll();
        model.addAttribute("campaigns", campaigns);
        return "homePage";
    }

    @GetMapping("/campaigns/create")
    public String createCampaignPage() {
        return "createCampaignPage";
    }
}
