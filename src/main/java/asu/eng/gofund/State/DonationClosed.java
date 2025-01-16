package asu.eng.gofund.State;

import asu.eng.gofund.controller.EmailNotificationService;
import asu.eng.gofund.controller.SMSNotificationService;
import asu.eng.gofund.model.Donation;

public class DonationClosed implements IDonationState{
    @Override
    public void NextState(Donation donation) {
        EmailNotificationService emailNotificationService = new EmailNotificationService();
        emailNotificationService.sendNotification(userRepo.findById(campaign.getStarterId()).get().getEmail(),"Donation Successful", campaign.getName(), amount);
        SMSNotificationService smsNotification = new SMSNotificationService();
        smsNotification.sendNotification(userRepo.findById(campaign.getStarterId()).get().getPhoneNumber(), "Donation Successful", campaign.getName(), amount);
    }

    @Override
    public void PrevState(Donation donation) {

    }
}
