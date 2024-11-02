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

    @OneToOne
    private Person author;
    private Date timestamp;
    @ManyToOne
    @JoinColumn(name = "id")
    private Campaign campaign;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies;
    private boolean edited;

    public Comment(){}

    public Comment(long id,
                   boolean isDeleted,
                   String content,
                   Person author,
                   Date timestamp,
                   Campaign campaign,
                   Comment parentComment,
                   List<Comment> replies,
                   boolean edited) {
        this.id = id;
        this.isDeleted = isDeleted;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.campaign = campaign;
        this.parentComment = parentComment;
        this.replies = replies;
        this.edited = edited;
    }

    public Comment(boolean isDeleted,
                   String content,
                   Person author,
                   Date timestamp,
                   Campaign campaign,
                   Comment parentComment,
                   List<Comment> replies,
                   boolean edited) {
        this.isDeleted = isDeleted;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.campaign = campaign;
        this.parentComment = parentComment;
        this.replies = replies;
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

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
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
}
