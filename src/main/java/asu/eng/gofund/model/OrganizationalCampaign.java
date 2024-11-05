package asu.eng.gofund.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "organizationalCampaign")
public class OrganizationalCampaign extends Campaign {


    public OrganizationalCampaign() {
        super();
    }

    public OrganizationalCampaign(boolean isDeleted, String name, String description, String imageUrl,
                                  CampaignStatus status, Currency currency, Long category, Long starterId,
                                  String bankAccountNumber, List<Address> addresses, List<Comment> comments) {
        super(isDeleted, name, description, imageUrl, status, currency, category, starterId, bankAccountNumber, addresses, comments);
    }


}
