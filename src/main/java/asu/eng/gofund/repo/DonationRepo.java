package asu.eng.gofund.repo;

import asu.eng.gofund.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRepo extends JpaRepository<Donation, Long> {
    List<Donation> getAllByIsRefundedFalseAndCampaignId(Long campaignId);
}
