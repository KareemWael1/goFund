package asu.eng.gofund.repo;
import asu.eng.gofund.model.PersonalCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalCampaignRepo extends JpaRepository<PersonalCampaign, Long> {

}