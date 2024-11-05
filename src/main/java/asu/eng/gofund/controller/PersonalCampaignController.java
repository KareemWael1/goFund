package asu.eng.gofund.controller;

import asu.eng.gofund.model.PersonalCampaign;
import asu.eng.gofund.repo.PersonalCampaignRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/personalCampaign")
public class PersonalCampaignController {

    @Autowired
    private PersonalCampaignRepo PersonalCampaignRepo;

    @PostMapping
    public String createPersonalCampaign(@RequestBody PersonalCampaign personalCampaign) {
        try {
            PersonalCampaign result = PersonalCampaignRepo.save(personalCampaign);
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
        return PersonalCampaignRepo.findAll();
    }

    @GetMapping("/{id}")
    public PersonalCampaign getPersonalCampaign(@PathVariable Long id) {
        return PersonalCampaign.getPersonalCampaignById(id);
    }

    @PutMapping
    public String updatePersonalCampaign(@RequestBody PersonalCampaign personalCampaign) {
        Long result = PersonalCampaignRepo.save(personalCampaign).getId();
        if (result == 1) {
            return "PersonalCampaign updated successfully";
        } else {
            return "PersonalCampaign update failed";
        }
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

}
