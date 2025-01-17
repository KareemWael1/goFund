package asu.eng.gofund.util;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.DonationRepo;
import asu.eng.gofund.repo.UserRepo;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class SpringContext {

    private static UserRepo userRepo;
    private static CampaignRepo campaignRepo;
    private static DonationRepo donationRepo;

    @Autowired
    public SpringContext(UserRepo userRepo, CampaignRepo campaignRepo, DonationRepo donationRepo) {
        SpringContext.userRepo = userRepo;
        SpringContext.campaignRepo = campaignRepo;
        SpringContext.donationRepo = donationRepo;
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
}