package asu.eng.gofund.repo;

import asu.eng.gofund.model.PersonalDonation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalDonationRepo extends JpaRepository<PersonalDonation, Long> {
}
