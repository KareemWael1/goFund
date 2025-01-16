package asu.eng.gofund.repo;
import asu.eng.gofund.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findAllByIsDeletedFalseOrderByIdAsc();

    List<Comment> findByParentCommentId(Long parentCommentId);

    List<Comment> findByCampaignIdAndIsDeletedFalse(Long campaignId);

    List<Comment> findByAuthorIdAndIsDeletedFalseOrderByIdAsc(Long authorId);
}
