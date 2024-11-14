package asu.eng.gofund.repo;

import asu.eng.gofund.model.OrgDonation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrgDonationRepo extends JpaRepository<OrgDonation, Long> {
}
