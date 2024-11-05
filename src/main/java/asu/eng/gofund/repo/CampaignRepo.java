package asu.eng.gofund.repo;
import asu.eng.gofund.model.Campaign;
import asu.eng.gofund.model.PersonalCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CampaignRepo extends JpaRepository<PersonalCampaign, Long> {

}