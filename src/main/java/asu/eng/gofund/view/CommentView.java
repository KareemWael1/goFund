package asu.eng.gofund.view;

import asu.eng.gofund.controller.CommentController;
import asu.eng.gofund.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentView {
    @Autowired
    private CommentController commentController;


    @PostMapping
    public String createComment(@RequestBody Comment comment) {
        Comment result = commentController.createComment(comment);
        if (result != null) {
            return "Comment created successfully";
        } else {
            return "Comment creation failed";
        }
    }

    @GetMapping("/{id}")
    public List<Comment> getCommentReplies(@PathVariable Long id) {
        return commentController.getCommentReplies(id);
    }
}
