package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.Comment;
import asu.eng.gofund.model.User;
import asu.eng.gofund.repo.CampaignRepo;
import asu.eng.gofund.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import asu.eng.gofund.repo.CommentRepo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

//    public CommentController() {
//    }

    @GetMapping("")
    public String commentPage() {
        return "hello";
    }

    @PostMapping (value = "",
            consumes = MediaType. APPLICATION_FORM_URLENCODED_VALUE)
    public String addComment(@RequestParam String content, @RequestParam String redirectUrl, @RequestParam String campaignId, @CurrentUser User user) {
        Comment comment = new Comment();
        comment.setCampaignId(Long.parseLong(campaignId));
        comment.setContent(content);
        comment.setAuthorId(user.getId());
        comment.setTimestamp((java.sql.Date) new Date(System.currentTimeMillis()));
        commentRepo.save(comment);
        System.out.println("Bruh"+redirectUrl);
        return "redirect:" + redirectUrl;
    }

//    @GetMapping("replies/{id}")
//    public List<Comment> getCommentReplies(@PathVariable Long id) {
//        return commentRepo.findByParentCommentId(id);
//    }
//
    @GetMapping("/{id}")
    public List<Comment> getComments(@PathVariable Long id) {
        return commentRepo.findByCampaignId(id);
    }
//
//    @DeleteMapping("/{commentId}")
//    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @CurrentUser User user) {
//        try {
//            Comment comment = commentRepo.findById(commentId).
//                    orElseThrow(() -> new RuntimeException("Comment not found"));
//            if (Objects.equals(user.getId(), comment.getAuthorId()) || user.getRole().equals("admin")) {
//                commentRepo.deleteById(commentId);
//                return ResponseEntity.ok("Comment deleted successfully");
//            } else {
//                throw new RuntimeException("User not authorized to delete comment");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//    }


}
