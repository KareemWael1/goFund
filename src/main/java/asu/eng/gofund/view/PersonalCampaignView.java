package asu.eng.gofund.view;

import asu.eng.gofund.controller.PersonalCampaignController;
import asu.eng.gofund.model.PersonalCampaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/personalCampaign")
public class PersonalCampaignView {
    public PersonalCampaignView() {
    }

    @Autowired
    private PersonalCampaignController personalCampaignController;




    @PostMapping
    public String createPersonalCampaign(@RequestBody PersonalCampaign personalCampaign) {
        try {
            PersonalCampaign result = personalCampaignController.createPersonalCampaign(personalCampaign);
            if (result != null) {
                return "PersonalCampaign created successfully";
            } else {
                return "PersonalCampaign creation failed";
            }
        } catch (Exception e) {
            return "Error creating PersonalCampaign: " + e.getMessage();
        }

    }

    @GetMapping
    public List<PersonalCampaign> getAllPersonalCampaigns() {
        return personalCampaignController.getAllPersonalCampaigns();
    }

    @GetMapping("/{id}")
    public PersonalCampaign getPersonalCampaign(@PathVariable Long id) {
        return personalCampaignController.getPersonalCampaign(id);
    }

    @PutMapping
    public String updatePersonalCampaign(@RequestBody PersonalCampaign personalCampaign) {
        Long result = personalCampaignController.updatePersonalCampaign(personalCampaign);
        if (result == 1) {
            return "PersonalCampaign updated successfully";
        } else {
            return "PersonalCampaign update failed";
        }
    }

    @DeleteMapping("/{id}")
    public String deletePersonalCampaign(@PathVariable Long id) {
        int result = personalCampaignController.deletePersonalCampaign(id);
        if (result == 1) {
            return "PersonalCampaign deleted successfully";
        } else {
            return "PersonalCampaign deletion failed";
        }

    }
}