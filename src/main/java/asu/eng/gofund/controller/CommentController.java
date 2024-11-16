package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.Comment;
import asu.eng.gofund.model.User;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import asu.eng.gofund.repo.CommentRepo;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        Long id = comment.getCampaignId();
        try {
            campaignRepo.findById(id);
            System.out.println("Campaign found");
        } catch (Exception e) {
            System.out.println("Campaign not found");
            return null;
        }

        return commentRepo.save(comment);

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
