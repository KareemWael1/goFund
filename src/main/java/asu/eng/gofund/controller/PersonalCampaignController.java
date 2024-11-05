package asu.eng.gofund.controller;

import asu.eng.gofund.model.PersonalCampaign;
import asu.eng.gofund.repo.CampaignRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PersonalCampaignController {

    @Autowired
    private CampaignRepo CampaignRepo;

    public PersonalCampaignController() {
    }

    public PersonalCampaign createPersonalCampaign(PersonalCampaign personalCampaign) {
        return CampaignRepo.save(personalCampaign);
    }

    public int deletePersonalCampaign(Long id) {
        return PersonalCampaign.deletePersonalCampaign(id);
    }

    public PersonalCampaign getPersonalCampaign(Long id) {
        return PersonalCampaign.getPersonalCampaignById(id);
    }

    public List<PersonalCampaign> getAllPersonalCampaigns() {
        return CampaignRepo.findAll();
//        return PersonalCampaign.getAllPersonalCampaigns();
    }

    public Long updatePersonalCampaign(PersonalCampaign personalCampaign) {
        return CampaignRepo.save(personalCampaign).getId();
//        return PersonalCampaign.updatePersonalCampaign(personalCampaign);
    }
}
