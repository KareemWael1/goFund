package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.Comment;
import asu.eng.gofund.model.Commenting.CommandExecutor;
import asu.eng.gofund.model.Commenting.CreateCommentCommand;
import asu.eng.gofund.model.Commenting.DeleteCommentCommand;
import asu.eng.gofund.model.User;
import asu.eng.gofund.model.UserType;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.UserRepo;
import asu.eng.gofund.view.CoreView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import asu.eng.gofund.repo.CommentRepo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private CampaignRepo campaignRepo;
    @Autowired
    private UserRepo userRepo;

    CoreView coreView = new CoreView();

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addComment(@RequestParam("redirectUrl") String redirectUrl,
            @RequestParam("campaignId") String campaignId,
            @RequestParam("content") String content,
            @RequestParam(value = "parentCommentId", required = false) Long parentCommentId,
            @CurrentUser User user) {
        Comment comment = new Comment();
        comment.setCampaignId(Long.parseLong(campaignId));
        comment.setContent(content);
        comment.setAuthorId(user.getId());
        comment.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
        comment.setParentCommentId(parentCommentId != null ? parentCommentId : 0L);
        CreateCommentCommand createCommentCommand = new CreateCommentCommand(commentRepo, comment);
        CommandExecutor.executeCommand(createCommentCommand, user.getId());
        return coreView.redirectToCertainPath(redirectUrl);
    }

    @GetMapping("/{id}")
    public List<Comment> getComments(@PathVariable Long id) {
        return commentRepo.findByCampaignIdAndIsDeletedFalse(id);
    }

    @DeleteMapping("/{commentId}")
    public RedirectView deleteComment(
            @PathVariable Long commentId,
            @RequestParam("redirectURI") String redirectURI,
            @CurrentUser User user,
            RedirectAttributes attributes) {
        try {
            Comment comment = commentRepo.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Comment not found"));
            if (Objects.equals(user.getId(), comment.getAuthorId()) ||
                    user.getUserType().comparePredefinedTypes(UserType.PredefinedType.ADMIN)) {
                DeleteCommentCommand deleteCommentCommand = new DeleteCommentCommand(commentRepo, comment);
                CommandExecutor.executeCommand(deleteCommentCommand, user.getId());
                return coreView.redirectTo(redirectURI);
            } else {
                return coreView.redirectTo("/error");
            }
        } catch (Exception e) {
            return coreView.redirectTo("/error");
        }

    }

    @PostMapping("/undo")
    public RedirectView undo(@CurrentUser User user) {
        CommandExecutor.undoLastCommand(user.getId());
        return coreView.redirectTo("/comment/history");
    }

    @PostMapping("/redo")
    public RedirectView redo(@CurrentUser User user) {
        CommandExecutor.redoLastCommand(user.getId());
        return coreView.redirectTo("/comment/history");
    }

    @GetMapping("/history")
    public String viewHistory(@CurrentUser User user, Model model) {
        List<String> history = CommandExecutor.getAuditLog(user.getId());
        model.addAttribute("history", history);
        return "commentHistory"; // Corresponding view template
    }
}
