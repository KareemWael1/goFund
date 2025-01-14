package asu.eng.gofund.model.Commenting;

import asu.eng.gofund.model.Comment;
import asu.eng.gofund.repo.CommentRepo;

public class DeleteCommentCommand implements CommentCommand {

    private final CommentRepo commentRepo;
    private final Comment comment;

    public DeleteCommentCommand(CommentRepo commentRepo, Comment comment) {
        this.comment = comment;
        this.commentRepo = commentRepo;
    }

    @Override
    public void execute() {
        comment.setDeleted(true);
        commentRepo.save(comment);
    }

    @Override
    public void undo() {
        comment.setDeleted(false);
        commentRepo.save(comment);
    }

    @Override
    public String getDescription() {
        return "Deleted a comment on post " + comment.getCampaignId() + " with content: " + comment.getContent();
    }
}
