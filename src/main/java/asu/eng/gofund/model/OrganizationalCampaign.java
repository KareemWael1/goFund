package asu.eng.gofund.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "organizationalCampaign")
public class OrganizationalCampaign extends Campaign {


    public OrganizationalCampaign() {
        super();
    }

    public OrganizationalCampaign(Long id, String name, String description, String imageUrl,
                                  CampaignStatus status, Currency currency, Long category, Long starterId,
                                  String bankAccountNumber, List<Address> addresses) {
        super(id, name, description, imageUrl, status, currency, category, starterId, bankAccountNumber,addresses);
    }
}
