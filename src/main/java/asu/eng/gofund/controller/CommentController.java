package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.Comment;
import asu.eng.gofund.model.User;
import asu.eng.gofund.model.UserType;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import asu.eng.gofund.repo.CommentRepo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
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

    @GetMapping("")
    public String commentPage() {
        return "hello";
    }

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
        commentRepo.save(comment);
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/{id}")
    public List<Comment> getComments(@PathVariable Long id) {
        return commentRepo.findByCampaignId(id);
    }

    @DeleteMapping("/{commentId}")
    public RedirectView deleteComment(
            @PathVariable Long commentId,
            @RequestParam("redirectURI") String redirectURI,
            @CurrentUser User user,
            RedirectAttributes attributes
    ) {
        try {
            Comment comment = commentRepo.findById(commentId).
                    orElseThrow(() -> new RuntimeException("Comment not found"));
            if (Objects.equals(user.getId(), comment.getAuthorId()) || user.getUserType().getValue() == UserType.Admin.getValue()) {
                comment.setIsDeleted(true);
                commentRepo.save(comment);
                return new RedirectView(redirectURI);
            } else {
                return new RedirectView("/error");
            }
        } catch (Exception e) {
            return new RedirectView("/error");
        }

    }
}
