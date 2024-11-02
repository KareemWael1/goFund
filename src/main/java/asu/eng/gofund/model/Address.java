package asu.eng.gofund.model;

import asu.eng.gofund.util.DatabaseUtil;
import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "\"parent_id\"")
    private Long parentId;


    public Address() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Address(Long id, String name, Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public Address(String name, Long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public String getCountry() {
        // TODO: Implement this method
        return "USA";
    }

    public String getCity() {
        // TODO: Implement this method
        return "New York";
    }
    public String getFullAddress() {
        // TODO: Implement this method
        return "123 Main St, New York, NY 10001";
    }

    public int saveAddress() {
        String sql = "INSERT INTO address (name, parent_id) VALUES (?, ?)";
        return DatabaseUtil.jdbcTemplate.update(sql, this.getName(), this.getParentId());

    }
}