package asu.eng.gofund.repo;

import asu.eng.gofund.model.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilestonesRepo extends JpaRepository<Milestone, Long> {
    List<Milestone> getAllByCampaignId(Long campaignId);

    Milestone findMilestoneById(Long parentId);
}
