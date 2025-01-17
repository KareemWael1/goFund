package asu.eng.gofund.State;

import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.Donation;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.DonationRepo;
import asu.eng.gofund.util.SpringContext;

public class SyncDonation implements IDonationState {

    private CampaignRepo campaignRepo;
    private DonationRepo donationRepo;


    public SyncDonation() {
        this.campaignRepo  = SpringContext.getCampaignRepo();
        this.donationRepo   = SpringContext.getDonationRepo();
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

        donationRepo.save(donation);
        campaign.donate(donation.getAmount());
        campaignRepo.save(campaign);
    }

}
