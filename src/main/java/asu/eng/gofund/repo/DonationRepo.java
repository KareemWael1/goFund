package asu.eng.gofund.repo;

import asu.eng.gofund.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepo extends JpaRepository<Donation, Long> {
}
