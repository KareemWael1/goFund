package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.*;
import asu.eng.gofund.model.Commenting.CommandExecutor;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.CommentRepo;
import asu.eng.gofund.repo.DonationRepo;
import asu.eng.gofund.repo.UserRepo;
import asu.eng.gofund.repo.UserTypeRepo;
import asu.eng.gofund.view.ControlPanelView;
import asu.eng.gofund.view.CoreView;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ControlPanelController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CampaignRepo campaignRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private DonationRepo donationRepo;

    @Autowired
    private UserTypeRepo userTypeRepo;

    ControlPanelView controlPanel = new ControlPanelView();

    CoreView coreView = new CoreView();

    @GetMapping("/control-panel")
    public String controlPanel(Model model, @CurrentUser User user, HttpServletRequest request) {
        model.addAttribute("user", user);
        model.addAttribute("redirectURI", request.getRequestURI());
        model.addAttribute("userTypes", userTypeRepo.findAll());

        if (user.getUserType().comparePredefinedTypes(UserType.PredefinedType.ADMIN)) {
            model.addAttribute("donationStats", calculateDonationStats());
            model.addAttribute("users", userRepo.findAllByDeletedFalse());
            model.addAttribute("campaigns", campaignRepo.findAllByDeletedFalseOrderByIdAsc());
            model.addAttribute("comments", commentRepo.findAllByIsDeletedFalseOrderByIdAsc());
            model.addAttribute("logs", getLogs());
            model.addAttribute("donations", donationRepo.getAllByIsRefundedFalse());
        } else if (user.getUserType().comparePredefinedTypes(UserType.PredefinedType.CAMPAIGN_CREATOR)) {
            model.addAttribute("campaigns", campaignRepo.findByStarterIdAndDeletedFalseOrderByIdAsc(user.getId()));
            model.addAttribute("comments", commentRepo.findByAuthorIdAndIsDeletedFalseOrderByIdAsc(user.getId()));
        } else if (user.getUserType().comparePredefinedTypes(UserType.PredefinedType.BASIC)) {
            model.addAttribute("comments", commentRepo.findByAuthorIdAndIsDeletedFalseOrderByIdAsc(user.getId()));
        } else {
            return coreView.redirectToHomePage();
        }

        return controlPanel.showControlPanel();
    }

    public List<String> getLogs() {
        List<String> logs = new ArrayList<>();
        CommandExecutor commandExecutor = new CommandExecutor();
        Iterator<String> iterator = commandExecutor.createLogsIterator();
        Campaign campaign = new Campaign();
        Iterator<String> iterator1 = campaign.createLogsIterator();
        while (iterator.hasNext()) {
            logs.add(iterator.next());
        }
        while (iterator1.hasNext()) {
            logs.add(iterator1.next());
        }
        return logs;
    }

    private Map<String, Object> calculateDonationStats() {
        Map<String, Object> stats = new HashMap<>();
        List<Donation> donations = donationRepo.findAll();

        // Total donations and amounts
        stats.put("totalDonations", donations.size());
        double totalAmount = donations.stream()
                .mapToDouble(Donation::getAmount)
                .sum();
        stats.put("totalAmount", String.format("%.2f", totalAmount));
        stats.put("averageDonation", String.format("%.2f",
                donations.isEmpty() ? 0 : totalAmount / donations.size()));



        // Payment method distribution
        Map<String, Long> paymentMethods = donations.stream()
                .collect(Collectors.groupingBy(
                        Donation::getPaymentStrategy,
                        Collectors.counting()
                ));
        stats.put("paymentMethods", paymentMethods);


        // Monthly donations
        Map<String, Double> monthlyDonations = donations.stream()
                .collect(Collectors.groupingBy(
                        d -> d.getDonationDate().format(DateTimeFormatter.ofPattern("MMM yyyy")),
                        Collectors.summingDouble(Donation::getAmount)
                ));
        stats.put("monthlyDonations", monthlyDonations);


        // Top donors
        Map<Long, Double> topDonors = donations.stream()
                .collect(Collectors.groupingBy(
                        Donation::getDonorId,
                        Collectors.summingDouble(Donation::getAmount)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        stats.put("topDonors", topDonors);


        // Currency distribution
        Map<CustomCurrency, Long> currencyDistribution = donations.stream()
                .collect(Collectors.groupingBy(
                        Donation::getCurrency,
                        Collectors.counting()
                ));
        stats.put("currencyDistribution", currencyDistribution);

        return stats;
    }

}
