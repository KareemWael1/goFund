package asu.eng.gofund.controller;

import asu.eng.gofund.controller.Payment.EGP2USDConverter;
import asu.eng.gofund.controller.Payment.IPaymentStrategy;
import asu.eng.gofund.controller.Payment.USD2EGPConverter;
import asu.eng.gofund.model.*;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.DonationRepo;
import asu.eng.gofund.repo.UserRepo;
import asu.eng.gofund.view.CampaignView;
import asu.eng.gofund.view.CoreView;
import asu.eng.gofund.view.DonationView;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/donate")
public class DonationController {
    @Autowired
    private DonationRepo donationRepo;

    @Autowired
    private CampaignRepo campaignRepo;

    @Autowired
    private CommentController commentController;

    @Autowired
    private UserRepo userRepo;

    DonationView donationView = new DonationView();
    CoreView coreView = new CoreView();
    CampaignView campaignView = new CampaignView();

    @PostMapping("/")
    public String donate(Model model,
                         @RequestParam String paymentStrategy,
                         @RequestParam Long userId,
                         @RequestParam Long campaignId,
                         @RequestParam double amount,
                         @RequestParam Long currency,
                         @RequestParam(required = false) Long campaignStarterId,
                         @RequestParam(required = false) Boolean regularDonation,
                         @RequestParam(required = false) String donationType,
                         @RequestParam(required = false) String cardNumber,
                         @RequestParam(required = false) String cardHolderName,
                         @RequestParam(required = false) String expiryDate,
                         @RequestParam(required = false) String cvv,
                         @RequestParam(required = false) String fawryCode,
                         @RequestParam(required = false) String fawryAccount,
                         @RequestParam(required = false) String fawryPassword
    ){
        IPaymentStrategy strategy;
        try {
            strategy = Donation.createPaymentStrategyFactory(paymentStrategy);
        } catch (Exception e) {
            model.addAttribute("error", "Invalid payment strategy");
            return coreView.showErrorPage();
        }
        CustomCurrency selectedCurrency = CustomCurrency.getCurrency(currency);
        if (regularDonation == null) {
            regularDonation = false;
        }
        if (donationType == null) {
            donationType = "personal";
        }
        LocalDateTime donationDate = LocalDateTime.now();
        Donation donation = Donation.createDonationFactory(donationType, userId, amount, campaignId, donationDate, selectedCurrency, false, paymentStrategy, campaignStarterId, regularDonation);
        Campaign campaign = campaignRepo.findById(donation.getCampaignId()).get();

        Donation decoratedDonation = handleDifferentCurrency(donation, campaign);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("cardNumber", cardNumber);
        credentials.put("cardHolderName", cardHolderName);
        credentials.put("expiryDate", expiryDate);
        credentials.put("cvv", cvv);
        credentials.put("fawryCode", fawryCode);
        credentials.put("fawryAccount", fawryAccount);
        credentials.put("fawryPassword", fawryPassword);
        if (decoratedDonation.executePayment(strategy, credentials, amount)) {
            donationRepo.save(donation);
            campaign.donate(decoratedDonation.getAmount());
            campaignRepo.save(campaign);
            return campaignView.redirectToCampaignWithID(campaignId);
        } else {
            model.addAttribute("error", "An error occurred while making the donation.");
            return coreView.showErrorPage();
        }
    }

    private Donation handleDifferentCurrency(Donation donation, Campaign campaign) {
        if (campaign.getCurrency() == CustomCurrency.USD && donation.getCurrency() == CustomCurrency.EGP) {
            donation = new EGP2USDConverter(donation);
        } else if (campaign.getCurrency() == CustomCurrency.EGP && donation.getCurrency() == CustomCurrency.USD) {
            donation = new USD2EGPConverter(donation);
        }
        return donation;
    }

    @GetMapping("/{campaignId}")
    public String showDonateForm(Model model, @PathVariable Long campaignId) {
        Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(campaignId);
        try {
            model.addAttribute("campaign", campaign);
            model.addAttribute("currencies", CustomCurrency.values());
            return donationView.showDonate();
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while preparing the donation form.");
            return coreView.showErrorPage();
        }
    }
}
