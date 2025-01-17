package asu.eng.gofund.util;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.DonationRepo;
import asu.eng.gofund.repo.MilestonesRepo;
import asu.eng.gofund.repo.UserRepo;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class SpringContext {

    private static UserRepo userRepo;
    private static CampaignRepo campaignRepo;
    private static DonationRepo donationRepo;
    private static MilestonesRepo milestonesRepo;

    @Autowired
    public SpringContext(UserRepo userRepo, CampaignRepo campaignRepo, DonationRepo donationRepo, MilestonesRepo milestonesRepo) {
        SpringContext.userRepo = userRepo;
        SpringContext.campaignRepo = campaignRepo;
        SpringContext.donationRepo = donationRepo;
        SpringContext.milestonesRepo = milestonesRepo;
    }

    public static UserRepo getUserRepo() {
        return userRepo;
    }

    public static CampaignRepo getCampaignRepo() {
        return campaignRepo;
    }

    public static DonationRepo getDonationRepo(){
        return donationRepo;
    }

    public static MilestonesRepo getMilestonesRepo() {
        return milestonesRepo;
    }
}