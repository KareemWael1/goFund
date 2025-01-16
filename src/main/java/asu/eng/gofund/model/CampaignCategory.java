package asu.eng.gofund.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.util.Pair;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "campaign_categories")
public class CampaignCategory {

    public enum PredefinedCategories {
        EDUCATION(1),
        HEALTH(2),
        ENVIRONMENT(3),
        ANIMAL_WELFARE(4),
        HUMANITARIAN(5),
        OTHER(6);

        private final int id;

        PredefinedCategories(int value) {
            this.id = value;
        }

        public int getId() {
            return this.id;
        }

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private String name;

    public CampaignCategory() {
    }

    public CampaignCategory(final String name) {
        this.name = name;
    }

    public CampaignCategory(final PredefinedCategories predefinedCategories) {
        this.name = predefinedCategories.name();
        this.id = (long) predefinedCategories.getId();
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
}
