package asu.eng.gofund.model.Commenting;

public interface CommentCommand {
    void execute();
    void undo();
    String getDescription();
}
