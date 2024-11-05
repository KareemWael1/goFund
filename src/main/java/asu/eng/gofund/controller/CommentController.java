package asu.eng.gofund.controller;

import asu.eng.gofund.model.Comment;
import asu.eng.gofund.repo.CampaignRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import asu.eng.gofund.repo.CommentRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private CampaignRepo campaignRepo;

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

    @GetMapping("/{id}")
    public List<Comment> getCommentReplies(@PathVariable Long id) {
        return commentRepo.findByParentCommentId(id);
    }


}
