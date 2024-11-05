package asu.eng.gofund.repo;

import asu.eng.gofund.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepo extends JpaRepository<Campaign, Long> {

}
