package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.Commenting.CommandExecutor;
import asu.eng.gofund.model.User;
import asu.eng.gofund.model.UserType;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

}
