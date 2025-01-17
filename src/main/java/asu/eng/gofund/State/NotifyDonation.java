package asu.eng.gofund.State;

import asu.eng.gofund.controller.EmailNotificationService;
import asu.eng.gofund.controller.SMSNotificationService;
import asu.eng.gofund.model.Donation;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.UserRepo;
import asu.eng.gofund.util.SpringContext;


public class NotifyDonation implements IDonationState{

    private UserRepo userRepo;

    private CampaignRepo campaignRepo;

    public NotifyDonation() {
        this.campaignRepo  = SpringContext.getCampaignRepo();
        this.userRepo   = SpringContext.getUserRepo();
    }

    @Override
    public void NextState(Donation donation) {

    }

    @Override
    public void PrevState(Donation donation) {

    }

    @Override
    public void ExecuteState(Donation donation) {

        EmailNotificationService emailNotificationService = new EmailNotificationService();
        emailNotificationService.sendNotification(userRepo.findById(donation.getDonorId()).get().getEmail(),"Donation Successful",campaignRepo.findById(donation.getCampaignId()).get().getName(), donation.getAmount());
        SMSNotificationService smsNotification = new SMSNotificationService();
        smsNotification.sendNotification(userRepo.findById(donation.getDonorId()).get().getPhoneNumber(), "Donation Successful", campaignRepo.findById(donation.getCampaignId()).get().getName(), donation.getAmount());

    }

}
