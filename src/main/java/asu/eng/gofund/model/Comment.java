package asu.eng.gofund.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean isDeleted;
    private String content;
    private Long authorId;
    private Date timestamp;
    private Long campaignId;
    private Long parentCommentId = 0L;
    private boolean edited;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId", insertable = false, updatable = false)
    private User user;


    @OneToMany(mappedBy = "parentCommentId")
    private List<Comment> replies;

    public List<Comment> getReplies() {
        List<Comment> notDeletedReplies = new ArrayList<>();
        for (Comment reply : replies) {
            if (!reply.isDeleted) {
                notDeletedReplies.add(reply);
            }
        }
        return notDeletedReplies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }
    public Comment(){}

    public Comment(long id,
                   boolean isDeleted,
                   String content,
                   Long authorId,
                   Date timestamp,
                   Long campaignId,
                   Long parentCommentId,
                   boolean edited) {
        this.id = id;
        this.isDeleted = isDeleted;
        this.content = content;
        this.authorId = authorId;
        this.timestamp = timestamp;
        this.campaignId = campaignId;
        this.parentCommentId = parentCommentId;
        this.edited = edited;
    }

    public Comment(boolean isDeleted,
                   String content,
                   Long authorId,
                   Date timestamp,
                   Long campaignId,
                   Long parentCommentId,
                   boolean edited) {
        this.isDeleted = isDeleted;
        this.content = content;
        this.authorId = authorId;
        this.timestamp = timestamp;
        this.campaignId = campaignId;
        this.parentCommentId = parentCommentId;
        this.edited = edited;
    }

}
