package asu.eng.gofund.repo;

import asu.eng.gofund.model.CampaignCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignCategoryRepo extends JpaRepository<CampaignCategory, Long> {
    CampaignCategory findByName(String name);
}
