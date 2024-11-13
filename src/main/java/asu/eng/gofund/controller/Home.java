package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.CampaignCategory;
import asu.eng.gofund.model.Sorting.CampaignSorter;
import asu.eng.gofund.model.Sorting.SortByMostBacked;
import asu.eng.gofund.model.Sorting.SortByMostRecent;
import asu.eng.gofund.model.Sorting.SortByOldest;
import asu.eng.gofund.model.User;
import asu.eng.gofund.repo.CampaignRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class Home {

    @Autowired
    private CampaignController campaignController;

    @GetMapping("/home")
    public String homePage(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) CampaignCategory filterCategory,
            @RequestParam(required = false) String filterTitle,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date filterEndDate,
            Model model,
            @CurrentUser User user,
            HttpServletRequest request) {



        // Call the getAllCampaigns method from CampaignController
        String viewName = campaignController.getAllCampaigns(sort, filterCategory, filterTitle, filterEndDate, model);

        if (user != null) {
            model.addAttribute("user", user);
        }

        return viewName;
    }

}