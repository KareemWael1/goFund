package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.CampaignCategory;
import asu.eng.gofund.model.User;
import asu.eng.gofund.model.UserType;
import asu.eng.gofund.repo.CampaignCategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/campaign-category")
public class CampaignCategoryController {

    @Autowired
    private CampaignCategoryRepo campaignCategoryRepo;

    @GetMapping
    public ResponseEntity<List<CampaignCategory>> getAllCampaignCategories() {
        return ResponseEntity.ok(campaignCategoryRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignCategory> getCampaignCategoryById(@PathVariable Long id) {
        Optional<CampaignCategory> campaignCategory = campaignCategoryRepo.findById(id);
        return campaignCategory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<CampaignCategory> createCampaignCategory(
            @RequestParam("campaignCategory") String campaignCategory,
            @CurrentUser User user) {
        if (!user.getUserType().comparePredefinedTypes(UserType.PredefinedType.ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(campaignCategoryRepo.save(new CampaignCategory(campaignCategory)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<CampaignCategory> updateCampaignCategory(@PathVariable Long id,
            @RequestParam("campaignCategory") String campaignCategory,
            @CurrentUser User user) {
        if (!user.getUserType().comparePredefinedTypes(UserType.PredefinedType.ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        Optional<CampaignCategory> existingCampaignCategory = campaignCategoryRepo.findById(id);
        if (existingCampaignCategory.isPresent()) {
            CampaignCategory updatedCampaignCategory = existingCampaignCategory.get();
            updatedCampaignCategory.setName(campaignCategory);
            return ResponseEntity.ok(campaignCategoryRepo.save(updatedCampaignCategory));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaignCategory(@PathVariable Long id, @CurrentUser User user) {
        if (!user.getUserType().comparePredefinedTypes(UserType.PredefinedType.ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        campaignCategoryRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
