package asu.eng.gofund.State;

import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.Donation;
import asu.eng.gofund.model.Milestone;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.DonationRepo;
import asu.eng.gofund.repo.MilestonesRepo;
import asu.eng.gofund.util.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SyncDonation implements IDonationState {

    private CampaignRepo campaignRepo;
    private DonationRepo donationRepo;
    private MilestonesRepo milestonesRepo;
    public SyncDonation() {
        this.campaignRepo  = SpringContext.getCampaignRepo();
        this.donationRepo   = SpringContext.getDonationRepo();
        this.milestonesRepo = SpringContext.getMilestonesRepo();
    }

    @Override
    public void NextState(Donation donation) {
        donation.setState(new NotifyDonation());
    }

    @Override
    public void PrevState(Donation donation) {

    }

    @Override
    public void ExecuteState(Donation donation) {

        Campaign campaign = campaignRepo.findById(donation.getCampaignId()).get();
        addDonationToMileStones(donation.getCampaignId(), donation);
        donationRepo.save(donation);
        campaign.donate(donation.getAmount());
        campaignRepo.save(campaign);
    }
    private void addDonationToMileStones(Long campaignId, Donation decoratedDonation) {
        List<Milestone> milestones = milestonesRepo.getAllByCampaignId(campaignId);
        double tempAmount = decoratedDonation.getAmount();
        for (Milestone milestone : milestones) {
            if (tempAmount <= 0) break;
            double remainingAmount = milestone.getTargetAmount() - milestone.getCurrentFunds();
            if (tempAmount >= remainingAmount) {
                milestone.setCurrentFunds(milestone.getTargetAmount());
                tempAmount -= remainingAmount;
            } else {
                milestone.setCurrentFunds(milestone.getCurrentFunds() + tempAmount);
                tempAmount = 0;
            }
            milestonesRepo.save(milestone);
        }
    }
}
