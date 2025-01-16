package asu.eng.gofund.repo;

import asu.eng.gofund.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepo extends JpaRepository<UserType, Long> {
    UserType findByName(String name);
}
