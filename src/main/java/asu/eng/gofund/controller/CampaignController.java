package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.*;
import asu.eng.gofund.model.CustomCurrency;
import asu.eng.gofund.model.Filtering.CampaignFilterer;
import asu.eng.gofund.model.Filtering.FilterByCategory;
import asu.eng.gofund.model.Filtering.FilterByEndDate;
import asu.eng.gofund.model.Filtering.FilterByTitleContains;
import asu.eng.gofund.model.Sorting.CampaignSorter;
import asu.eng.gofund.model.Sorting.SortByMostBacked;
import asu.eng.gofund.model.Sorting.SortByMostRecent;
import asu.eng.gofund.model.Sorting.SortByOldest;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.DonationRepo;
import asu.eng.gofund.repo.UserRepo;
import asu.eng.gofund.view.CampaignView;
import asu.eng.gofund.view.CoreView;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@Controller
@RequestMapping("/campaign")
public class CampaignController {

    @Autowired
    private CampaignRepo campaignRepo;
    @Autowired
    private DonationRepo donationRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CommentController commentController;

    CampaignView campaignView = new CampaignView();
    CoreView coreView = new CoreView();

    @GetMapping("")
    public String campaignPage(Model model) {
        List<Campaign> campaigns = campaignRepo.findAllByDeletedFalse();
        model.addAttribute("campaigns", campaigns);
        model.addAttribute("categories", CampaignCategory.values());
        return campaignView.showCampaigns();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView updateCampaign(
            @PathVariable Long id,
            @RequestParam("redirectURI") String redirectURI,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @CurrentUser User user,
            RedirectAttributes attributes
    ) {
        Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(id);
        if(campaign != null && (user.getUserType().getValue() == UserType.Admin.getValue() || campaign.getStarterId() == user.getId())) {
            if (name != null && !name.isEmpty()) {
                campaign.setName(name);
            }
            if (description != null && !description.isEmpty()) {
                campaign.setDescription(description);
            }

            campaignRepo.save(campaign);
            return new RedirectView(redirectURI);
        }
        return coreView.redirectTo("/error");
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteCampaign(@PathVariable Long id, @RequestParam("redirectURI") String redirectURI, @CurrentUser User user) {
        Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(id);
        if (Objects.equals(campaign.getStarterId(), user.getId()) || Objects.equals(user.getUserType().getValue(), UserType.Admin.getValue())) {
            campaign.setDeleted(true);
            donationRepo.getAllByIsRefundedFalseAndCampaignId(id).forEach(donation -> {
                donation.setRefunded(true);
                donationRepo.save(donation);
            });
            campaignRepo.save(campaign);
            return new RedirectView(redirectURI);
        }

        return coreView.redirectTo("/error");

    }




    @GetMapping("/{id}")
    public String viewCampaignDetails(@PathVariable Long id, Model model, HttpServletRequest request, @CurrentUser User user) {
        Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(id);
        double percentage = Math.round((campaign.getCurrentAmount() / campaign.getTargetAmount()) * 100);
        List<Comment> comments = commentController.getComments(id).stream()
                .filter(comment -> comment.getParentCommentId() == 0)
                .collect(Collectors.toList());
        if (campaign.getObservers().contains(user)) {
            model.addAttribute("subscribed", true);
        }else {
            model.addAttribute("subscribed", false);
        }
        model.addAttribute("percentage", percentage);
        model.addAttribute("campaign", campaign);
        model.addAttribute("comments", comments);
        model.addAttribute("requestURI", request.getRequestURI());
        return campaignView.showCampaignDetails();
    }



    @GetMapping("/list")
    public String getAllCampaigns(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) CampaignCategory filterCategory,
            @RequestParam(required = false) String filterTitle,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date filterEndDate,
            Model model) {

        List<Campaign> campaigns = campaignRepo.findAllByDeletedFalse();

        CampaignSorter campaignSorter = new CampaignSorter(null);
        CampaignFilterer campaignFilterer = new CampaignFilterer(null);

        if (filterCategory != null) {
            campaignFilterer.setStrategy(new FilterByCategory(filterCategory));
            campaigns = campaignFilterer.filterCampaigns(campaigns);
        }
        if (filterEndDate != null) {
            campaignFilterer.setStrategy(new FilterByEndDate(filterEndDate));
            campaigns = campaignFilterer.filterCampaigns(campaigns);
        }
        if (filterTitle != null) {
            campaignFilterer.setStrategy(new FilterByTitleContains(filterTitle));
            campaigns = campaignFilterer.filterCampaigns(campaigns);
        }

        if ("mostRecent".equalsIgnoreCase(sort)) {
            campaignSorter.setStrategy(new SortByMostRecent());
            campaigns = campaignSorter.sortCampaigns(campaigns);
        } else if ("oldest".equalsIgnoreCase(sort)) {
            campaignSorter.setStrategy(new SortByOldest());
            campaigns = campaignSorter.sortCampaigns(campaigns);
        } else if ("mostBacked".equalsIgnoreCase(sort)) {
            campaignSorter.setStrategy(new SortByMostBacked());
            campaigns = campaignSorter.sortCampaigns(campaigns);
        }


        model.addAttribute("campaigns", campaigns);
        model.addAttribute("categories", CampaignCategory.values());
        return campaignView.showCampaigns();
    }


    @PutMapping("/{campaignId}/donate")
    public ResponseEntity<String> donateToCampaign(
            @PathVariable Long campaignId,
            @CurrentUser User user,
            @RequestParam double amount) {

        try {
            Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(campaignId);
            campaign.donate(amount);
            campaignRepo.save(campaign);
            return ResponseEntity.ok("Donation successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Donation failed "+ e.getMessage());
        }

    }

    @PostMapping("/{campaignId}/subscribe")
    public ResponseEntity<String> subscribeToCampaign(
            @PathVariable Long campaignId,
            @CurrentUser User user) {

        try {
            Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(campaignId);
            User currUser = userRepo.findById(user.getId()).get();
            currUser.subscribe(campaign);
            campaignRepo.save(campaign);
            return ResponseEntity.ok("User subscribed to campaign successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Subscription failed " + e.getMessage());
        }
    }

    @PostMapping("/{campaignId}/unsubscribe")
    public ResponseEntity<String> unsubscribeFromCampaign(
            @PathVariable Long campaignId,
            @CurrentUser User user) {

        try {
            Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(campaignId);
            User currUser = userRepo.findById(user.getId()).get();
            currUser.unsubscribe(campaign);
            campaignRepo.save(campaign);
            return ResponseEntity.ok("User unsubscribed from campaign successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unsubscription failed " + e.getMessage());
        }
    }

    @GetMapping("/create")
    public String showCreateCampaignForm(Model model) {
        try {
            model.addAttribute("categories", CampaignCategory.values());
            model.addAttribute("currencies", CustomCurrency.values());
            return campaignView.showCreateCampaign();
        } catch (Exception e) {
            return coreView.showErrorPage();
        }
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createCampaign(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") Long category,
            @RequestParam("currency") Long currency,
            @RequestParam("imageUrl") String imageUrl,
            @RequestParam("targetAmount") double targetAmount,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            Model model,
            @CurrentUser User user) {
        try {
            Campaign campaign = new Campaign();
            campaign.setName(name);
            campaign.setDescription(description);
            campaign.setCategory(CampaignCategory.getCategory(category));
            campaign.setCurrency(CustomCurrency.getCurrency(currency));
            campaign.setImageUrl(imageUrl);
            campaign.setTargetAmount(targetAmount);
            campaign.setEndDate(endDate);
            campaign.setStarterId(user.getId());
            campaignRepo.save(campaign);
            return campaignView.redirectToCampaign();
        } catch (Exception e) {
            return coreView.showErrorPage();
        }
    }

    @PostMapping("/{campaignId}/close")
    public String closeCampaign(@PathVariable Long campaignId, @CurrentUser User user) {
        try {
            Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(campaignId);
            if (campaign.getStarterId().equals(user.getId())) {
                campaign.closeCampaign();
                campaignRepo.save(campaign);
                return campaignView.redirectToCampaignWithID(campaignId);
            }
            return coreView.showErrorPage();
        } catch (Exception e) {
            return coreView.showErrorPage();
        }
    }

    @PostMapping("/{campaignId}/reopen")
    public String reopenCampaign(@PathVariable Long campaignId, @CurrentUser User user) {
        try {
            Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(campaignId);
            if (campaign.getStarterId().equals(user.getId())) {
                campaign.reopenCampaign();
                campaignRepo.save(campaign);
                return campaignView.redirectToCampaignWithID(campaignId);
            }
            return coreView.showErrorPage();
        } catch (Exception e) {
            return coreView.showErrorPage();
        }
    }

}
