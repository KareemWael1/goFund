package asu.eng.gofund.controller;

import asu.eng.gofund.model.PersonalCampaign;
import asu.eng.gofund.model.User;
import asu.eng.gofund.repo.PersonalCampaignRepo;
import asu.eng.gofund.repo.UserRepo;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/personalCampaign")
public class PersonalCampaignController {

    @Autowired
    private PersonalCampaignRepo PersonalCampaignRepo;
    @Autowired
    private UserRepo userRepo;

    @PostMapping
    public String createPersonalCampaign(@RequestBody PersonalCampaign personalCampaign) {
        try {
            PersonalCampaign result = PersonalCampaignRepo.save(personalCampaign);
            return "personalCampaign";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "error";
        }

    }

    @GetMapping
    public String getAllPersonalCampaigns(Model model) {
        List<PersonalCampaign> campaigns = PersonalCampaignRepo.findAll();
        model.addAttribute("campaigns", campaigns);
        return "personalCampaign";
    }

    @GetMapping("/{id}")
    public PersonalCampaign getPersonalCampaign(@PathVariable Long id) {
        return PersonalCampaign.getPersonalCampaignById(id);
    }

    @PutMapping
    public String updatePersonalCampaign(@RequestBody PersonalCampaign personalCampaign) {
        Long result = PersonalCampaignRepo.save(personalCampaign).getId();
        if (result == 1) {
            return "personalCampaign";
        } else {
            return "error";
        }
    }

    @PutMapping("/{campaignId}/donate/{userId}")
    public ResponseEntity<String> donateToCampaign(
            @PathVariable Long campaignId,
            @PathVariable Long userId,
            @RequestParam double amount) {
        // Retrieve the campaign and user from the repositories
        PersonalCampaign campaign = PersonalCampaignRepo.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // make operations on user if needed
        campaign.donate(amount);
        PersonalCampaignRepo.save(campaign);
        return ResponseEntity.ok("Donation successful");
    }


    @DeleteMapping("/{id}")
    public String deletePersonalCampaign(@PathVariable Long id) {
        int result = PersonalCampaign.deletePersonalCampaign(id);
        if (result == 1) {
            return "PersonalCampaign deleted successfully";
        } else {
            return "PersonalCampaign deletion failed";
        }

    }

    @PostMapping("/{campaignId}/subscribe/{userId}")
    public ResponseEntity<String> subscribeToCampaign(
            @PathVariable Long campaignId,
            @PathVariable Long userId) {

        // Retrieve the campaign and user from the repositories
        PersonalCampaign campaign = PersonalCampaignRepo.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Add the user as an observer of the campaign
        user.subscribe(campaign);

        // Save the updated campaign if needed
        PersonalCampaignRepo.save(campaign);

        return ResponseEntity.ok("User subscribed to campaign successfully");
    }

}
