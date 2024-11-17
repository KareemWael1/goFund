package asu.eng.gofund.controller;

import asu.eng.gofund.controller.Payment.EGP2USDConverter;
import asu.eng.gofund.controller.Payment.IPaymentStrategy;
import asu.eng.gofund.controller.Payment.USD2EGPConverter;
import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.CustomCurrency;
import asu.eng.gofund.model.Donation;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.DonationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/donate")
public class DonationController {
    @Autowired
    private DonationRepo donationRepo;

    @Autowired
    private CampaignRepo campaignRepo;

    @PostMapping("/")
    public String donate(Map<String, String> body, Model model) {
        IPaymentStrategy paymentStrategy;
        try {
            paymentStrategy = Donation.createPaymentStrategyFactory(body.get("paymentStrategy"));
        } catch (Exception e) {
            return "Invalid payment strategy";
        }
        Long userId = Long.parseLong(body.get("userId"));
        Long campaignId = Long.parseLong(body.get("campaignId"));
        double amount = Double.parseDouble(body.get("amount"));
        CustomCurrency currency = CustomCurrency.getCurrency(Long.parseLong(body.get("currency")));
        Long campaignStarterId = null;
        boolean regularDonation = false;
        if (body.get("campaignStarterId") == null) {
            campaignStarterId = Long.parseLong(body.get("campaignStarterId"));
        }
        if (body.get("regularDonation") == null) {
            regularDonation = Boolean.parseBoolean(body.get("regularDonation"));
        }

        LocalDateTime donationDate = LocalDateTime.now();
        Donation donation = Donation.createDonationFactory(body.get("donationType"), userId, amount, campaignId, donationDate, currency, false, body.get("paymentStrategy"), campaignStarterId, regularDonation);
        donation = handleDifferentCurrency(donation);
        if (donation.executePayment(paymentStrategy, body, amount)) {
            donationRepo.save(donation);
            return "redirect:/donationDetails/" + donation.getId() + "/";
        } else {
            model.addAttribute("error", "An error occurred while making the donation.");
            return "errorPage";
        }
    }

    private Donation handleDifferentCurrency(Donation donation) {
        Campaign campaign = campaignRepo.findById(donation.getCampaignId()).get();
        if (campaign.getCurrency() == CustomCurrency.USD && donation.getCurrency() == CustomCurrency.EGP) {
            donation = new EGP2USDConverter(donation);
        } else if (campaign.getCurrency() == CustomCurrency.EGP && donation.getCurrency() == CustomCurrency.USD) {
            donation = new USD2EGPConverter(donation);
        }
        return donation;
    }

    @GetMapping("")
    public String showDonateForm(Model model) {
        try {
            model.addAttribute("currencies", CustomCurrency.values());
            return "donate";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while preparing the donation form.");
            return "errorPage";
        }
    }
}
