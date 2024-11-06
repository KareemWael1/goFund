package asu.eng.gofund.controller;

import asu.eng.gofund.model.*;
import asu.eng.gofund.model.Filtering.*;
import asu.eng.gofund.model.Sorting.*;
import asu.eng.gofund.repo.CampaignRepo;
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

//    @GetMapping("")
//    public ResponseEntity<Iterable<Campaign>> getAllCampaigns() {
//        Iterable<Campaign> campaigns = campaignRepo.findAll();
//        return ResponseEntity.ok(campaigns);
//    }

    @GetMapping("")
    public ResponseEntity<Iterable<Campaign>> getAllCampaigns(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) CampaignCategory filterCategory,
            @RequestParam(required = false) String filterTitle,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date filterEndDate) {

        List<Campaign> campaigns = (List<Campaign>) campaignRepo.findAll();

        CampaignFilterer campaignFilterer = new CampaignFilterer(null);

        if (filterCategory != null) {
            campaignFilterer.setStrategy(new FilterByCategory(filterCategory));
            campaigns = campaignFilterer.filterCampaigns(campaigns);
        }
        if (filterTitle != null) {
            campaignFilterer.setStrategy(new FilterByTitleContains(filterTitle));
            campaigns = campaignFilterer.filterCampaigns(campaigns);
        }
        if (filterEndDate != null) {
            campaignFilterer.setStrategy(new FilterByEndDate(filterEndDate));
            campaigns = campaignFilterer.filterCampaigns(campaigns);
        }

        CampaignSorter campaignSorter = new CampaignSorter(null);

        if ("mostRecent".equalsIgnoreCase(sort)) {
            campaignSorter.setStrategy(new SortByMostRecent());
        } else if ("oldest".equalsIgnoreCase(sort)) {
            campaignSorter.setStrategy(new SortByOldest());
        } else if ("mostBacked".equalsIgnoreCase(sort)) {
            campaignSorter.setStrategy(new SortByMostBacked());
        }

        campaigns = campaignSorter.sortCampaigns(campaigns);
        
        return ResponseEntity.ok(campaigns);
    }

}
