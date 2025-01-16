package asu.eng.gofund.repo;

import asu.eng.gofund.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findAllByDeletedFalse();

    User findByUsername(String username);
}
