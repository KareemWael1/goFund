package asu.eng.gofund.model;

import asu.eng.gofund.util.DatabaseUtil;
import jakarta.persistence.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    // User attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    // Constructors
    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    // User DB operations

    public static List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return DatabaseUtil.jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public static User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return DatabaseUtil.jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
    }

    public int saveUser() {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        return DatabaseUtil.jdbcTemplate.update(sql, this.getName(), this.getEmail(), this.getPassword());
    }

    public int updateUser() {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, WHERE id = ?";
        return DatabaseUtil.jdbcTemplate.update(sql, this.getName(), this.getEmail(), this.getId(), this.getPassword());
    }

    public static int deleteUserById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return DatabaseUtil.jdbcTemplate.update(sql, id);
    }

}
