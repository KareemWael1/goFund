package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.Comment;
import asu.eng.gofund.model.User;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import asu.eng.gofund.repo.CommentRepo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class CommentController {

    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private CampaignRepo campaignRepo;
    @Autowired
    private UserRepo userRepo;

    public CommentController() {
    }


    @PostMapping("/campaign/{id}/comment/add")
    public String addComment(@PathVariable Long id, @RequestParam String content, @CurrentUser User user, Model model) {
        Comment comment = new Comment();
        comment.setCampaignId(id);
        comment.setContent(content);
        comment.setAuthorId(user.getId());
        comment.setTimestamp((java.sql.Date) new Date(System.currentTimeMillis()));
        commentRepo.save(comment);

        Campaign campaign = campaignRepo.findCampaignByIdAndDeletedFalse(id);
        double percentage = Math.round((campaign.getCurrentAmount() / campaign.getTargetAmount()) * 100);
        List<Comment> comments = commentRepo.findByCampaignId(id);

        model.addAttribute("percentage", percentage);
        model.addAttribute("campaign", campaign);
        model.addAttribute("comments", comments);
        return "campaignDetails";
    }

    @GetMapping("replies/{id}")
    public List<Comment> getCommentReplies(@PathVariable Long id) {
        return commentRepo.findByParentCommentId(id);
    }

    @GetMapping("/{id}")
    public List<Comment> getComments(@PathVariable Long id) {
        return commentRepo.findByCampaignId(id);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @CurrentUser User user) {
        try {
            Comment comment = commentRepo.findById(commentId).
                    orElseThrow(() -> new RuntimeException("Comment not found"));
            if (Objects.equals(user.getId(), comment.getAuthorId()) || user.getRole().equals("admin")) {
                commentRepo.deleteById(commentId);
                return ResponseEntity.ok("Comment deleted successfully");
            } else {
                throw new RuntimeException("User not authorized to delete comment");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


}
