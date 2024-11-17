package asu.eng.gofund.model;

import jakarta.persistence.*;

import java.sql.Date;
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
        return replies;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public List<Comment> getAllCommentsByCampaignID(long id){
        //TODO put some fancy sql here
        return null;
    }
    public boolean updateCommentById(long id){
        //TODO add some fancy sql here
        return true;
    }

    public boolean createComment(){
        //TODO add some fancy sql here
        return true;
    }

    public boolean deleteCommentById(long id){
        //TODO add some fancy sql here
        return true;
    }


    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public void setParentCommentId(long l) {
        this.parentCommentId = l;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
