package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.*;
import asu.eng.gofund.model.Currency;
import asu.eng.gofund.model.Filtering.CampaignFilterer;
import asu.eng.gofund.model.Filtering.FilterByCategory;
import asu.eng.gofund.model.Filtering.FilterByEndDate;
import asu.eng.gofund.model.Filtering.FilterByTitleContains;
import asu.eng.gofund.model.Sorting.CampaignSorter;
import asu.eng.gofund.model.Sorting.SortByMostBacked;
import asu.eng.gofund.model.Sorting.SortByMostRecent;
import asu.eng.gofund.model.Sorting.SortByOldest;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.UserRepo;
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
    private UserRepo userRepo;

    @GetMapping("")
    public String campaignPage(Model model) {
        List<Campaign> campaigns = campaignRepo.findAllByDeletedFalse();
        model.addAttribute("campaigns", campaigns);
        model.addAttribute("categories", CampaignCategory.values());
        return "campaign";
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
        return new RedirectView("/error");
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteCampaign(@PathVariable Long id, @RequestParam("redirectURI") String redirectURI, @CurrentUser User user) {
        Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(id);
        if (Objects.equals(campaign.getStarterId(), user.getId()) || Objects.equals(user.getUserType().getValue(), UserType.Admin.getValue())) {
            campaign.setDeleted(true);
            campaignRepo.save(campaign);
            return new RedirectView(redirectURI);
        }

        return new RedirectView("/error");

    }

    @Autowired
    private CommentController commentController;


    @GetMapping("/{id}")
    public String viewCampaignDetails(@PathVariable Long id, Model model, HttpServletRequest request) {
        Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(id);
        double percentage = Math.round((campaign.getCurrentAmount() / campaign.getTargetAmount()) * 100);
        List<Comment> comments = commentController.getComments(id).stream()
                .filter(comment -> comment.getParentCommentId() == 0)
                .collect(Collectors.toList());

        model.addAttribute("percentage", percentage);
        model.addAttribute("campaign", campaign);
        model.addAttribute("comments", comments);
        model.addAttribute("requestURI", request.getRequestURI());
        return "campaignDetails";
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
        return "campaign";
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

    @PostMapping("/{campaignId}/subscribe/{userId}")
    public ResponseEntity<String> subscribeToCampaign(
            @PathVariable Long campaignId,
            @PathVariable Long userId) {

        try {
            Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(campaignId);

            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.subscribe(campaign);

            campaignRepo.save(campaign);

            return ResponseEntity.ok("User subscribed to campaign successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Subscription failed " + e.getMessage());
        }
    }

    @PostMapping("/{campaignId}/unsubscribe/{userId}")
    public ResponseEntity<String> unsubscribeFromCampaign(
            @PathVariable Long campaignId,
            @PathVariable Long userId) {

        try {
            Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(campaignId);

            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.unsubscribe(campaign);

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
            model.addAttribute("currencies", Currency.values());
            return "createCampaign";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while preparing the campaign creation form.");
            return "errorPage";
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
            Model model) {
        try {
            Campaign campaign = new Campaign();
            campaign.setName(name);
            campaign.setDescription(description);
            campaign.setCategory(CampaignCategory.getCategory(category));
            campaign.setCurrency(Currency.getCurrency(currency));
            campaign.setImageUrl(imageUrl);
            campaign.setTargetAmount(targetAmount);
            campaign.setEndDate(endDate);
            campaignRepo.save(campaign);
            return "redirect:/campaign";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while creating the campaign.");
            return "errorPage";
        }
    }

}
