package asu.eng.gofund.controller;

import asu.eng.gofund.model.Comment;
import asu.eng.gofund.repo.CampaignRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import asu.eng.gofund.repo.CommentRepo;

import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private CampaignRepo campaignRepo;

    public CommentController() {
    }

    public Comment createComment(Comment comment) {
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


    public List<Comment> getCommentReplies(Long id) {
        return commentRepo.findByParentCommentId(id);
    }
}
