package asu.eng.gofund.model;

import asu.eng.gofund.controller.EmailNotificationService;
import asu.eng.gofund.controller.SMSNotificationService;
import asu.eng.gofund.util.DatabaseUtil;
import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements Observer {

    // User attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean deleted;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String loginStrategy;
    @ManyToOne
    @JoinColumn(name = "user_type_id")
    private UserType userType;
    @ManyToMany(mappedBy = "observers")
    private List<Campaign> subjects;
    // Constructors

    public User(String name, String email, String phoneNumber, String password, String loginStrategy) {
        this.username = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.loginStrategy = loginStrategy;
        this.userType = userType;
    }

    public User(Long id, String name, String email, String password, String loginStrategy) {
        this.id = id;
        this.username = name;
        this.email = email;
        this.password = password;
        this.loginStrategy = loginStrategy;
    }

    public User() {

    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
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

    public String getLoginStrategy() {
        return loginStrategy;
    }

    public void setLoginStrategy(String loginStrategy) {
        this.loginStrategy = loginStrategy;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", loginStrategy='" + loginStrategy + '\'' +
                ", userType=" + userType +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    // User DB operations

    public static List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return DatabaseUtil.getConnection().query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public static User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return DatabaseUtil.getConnection().queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
    }

    public static User getUserByUsernameAndPassword(String username, String password) {
        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND deleted = false";
            return DatabaseUtil.getConnection().queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username,
                    password);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public static User getUserByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        return DatabaseUtil.getConnection().queryForObject(sql, new BeanPropertyRowMapper<>(User.class), email,
                password);
    }

    public int saveUser() {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        return DatabaseUtil.getConnection().update(sql, this.getUsername(), this.getEmail(), this.getPassword());
    }

    public int updateUser() {
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, WHERE id = ?";
        return DatabaseUtil.getConnection().update(sql, this.getUsername(), this.getEmail(), this.getId(),
                this.getPassword());
    }

    public static int deleteUserById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return DatabaseUtil.getConnection().update(sql, id);
    }

    @Override
    public void update(String campaignName, Double reachedAmount) {
        EmailNotificationService emailNotification = new EmailNotificationService();
        emailNotification.sendNotification(this.getEmail(), "Donation Update", campaignName, reachedAmount);
    }

    public void subscribe(Subject subject) {
        subjects.add((Campaign) subject);
        subject.registerObserver(this);
    }

    public void unsubscribe(Subject subject) {
        Campaign sub = subjects.get(subjects.indexOf((Campaign) subject));
        sub.removeObserver(this);
        subjects.remove(sub);

    }

    public String getRole() {
        // TODO: Implement this method
        return "user";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;

        // Handle Hibernate proxies
        User user;
        if (obj instanceof HibernateProxy) {
            user = (User) ((HibernateProxy) obj).getHibernateLazyInitializer()
                    .getImplementation();
        } else {
            user = (User) obj;
        }

        return id != null && id.equals(user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
