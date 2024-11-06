package asu.eng.gofund.repo;

import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.CampaignCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CampaignRepo extends JpaRepository<Campaign, Long> {

    @Query("SELECT c FROM Campaign c WHERE (:category IS NULL OR c.category = :category) " +
            "AND (:title IS NULL OR c.name LIKE %:title%) " +
            "AND (:endDate IS NULL OR c.endDate = :endDate)")
    List<Campaign> findByFilters(@Param("category") CampaignCategory category,
                                 @Param("title") String title,
                                 @Param("endDate") Date endDate);

    @Query("SELECT c FROM Campaign c ORDER BY c.startDate DESC")
    List<Campaign> findAllOrderByMostRecent();

    @Query("SELECT c FROM Campaign c ORDER BY c.startDate ASC")
    List<Campaign> findAllOrderByOldest();

    @Query("SELECT c FROM Campaign c ORDER BY c.currentAmount DESC")
    List<Campaign> findAllOrderByMostBacked();
}