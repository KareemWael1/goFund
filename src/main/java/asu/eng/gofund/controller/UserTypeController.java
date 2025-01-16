package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.User;
import asu.eng.gofund.model.UserType;
import asu.eng.gofund.repo.UserTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user-type")
public class UserTypeController {

    @Autowired
    private UserTypeRepo userTypeRepo;

    @GetMapping
    public ResponseEntity<List<UserType>> getAllUserTypes() {
        return ResponseEntity.ok(userTypeRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserType> getUserTypeById(@PathVariable Long id) {
        Optional<UserType> userType = userTypeRepo.findById(id);
        return userType.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<UserType> createUserType(@RequestParam("userType") String userType,
            @CurrentUser User user) {
        if (!user.getUserType().comparePredefinedTypes(UserType.PredefinedType.ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(userTypeRepo.save(new UserType(userType)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<UserType> updateUserType(@PathVariable Long id, @RequestParam("userType") String userType,
            @CurrentUser User user) {
        if (!user.getUserType().comparePredefinedTypes(UserType.PredefinedType.ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        Optional<UserType> existingUserType = userTypeRepo.findById(id);
        if (existingUserType.isPresent()) {
            UserType updatedUserType = existingUserType.get();
            updatedUserType.setName(userType);
            return ResponseEntity.ok(userTypeRepo.save(updatedUserType));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserType(@PathVariable Long id, @CurrentUser User user) {
        if (!user.getUserType().comparePredefinedTypes(UserType.PredefinedType.ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        userTypeRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
