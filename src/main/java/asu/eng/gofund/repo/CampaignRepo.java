package asu.eng.gofund.repo;

import asu.eng.gofund.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepo extends JpaRepository<Campaign, Long> {
//    List<Campaign> findAllByisDeletedFalse();
//    Campaign getCampaignByIdAndisDeletedFalse(Long id);
    List<Campaign> findAllByDeletedFalse();
    Campaign getCampaignsByIdAndDeletedFalse(Long id);
}
