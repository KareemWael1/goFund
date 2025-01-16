package asu.eng.gofund.model;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_types")
public class UserType {

    public enum PredefinedType {
        ADMIN("Admin", 1),
        CAMPAIGN_CREATOR("CampaignCreator", 2),
        BASIC("Basic", 3);

        private final String name;
        private final int id;

        PredefinedType(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private String name;

    public UserType() {
    }

    public UserType(final String name) {
        this.name = name;
    }

    public UserType(final PredefinedType predefinedType) {
        this.name = predefinedType.getName();
        this.id = (long) predefinedType.getId();
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean compare(UserType userType) {
        return this.id.equals(userType.getId());
    }

    public boolean comparePredefinedTypes(PredefinedType predefinedType) {
        return this.name.equalsIgnoreCase(predefinedType.getName());
    }
}
