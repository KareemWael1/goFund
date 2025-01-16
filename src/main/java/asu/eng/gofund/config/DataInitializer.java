package asu.eng.gofund.config;

import asu.eng.gofund.model.UserType;
import asu.eng.gofund.repo.UserTypeRepo;
import asu.eng.gofund.util.UserUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import asu.eng.gofund.model.CampaignCategory;
import asu.eng.gofund.model.User;
import asu.eng.gofund.repo.CampaignCategoryRepo;
import asu.eng.gofund.repo.UserRepo;
import org.springframework.beans.factory.annotation.Value;

import jakarta.annotation.PostConstruct;

@Configuration
public class DataInitializer {

    @Autowired
    private UserTypeRepo userTypeRepo;

    @Autowired
    private CampaignCategoryRepo campaignCategoryRepo;

    @Autowired
    private UserRepo userRepo;

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.email:admin@example.com}")
    private String adminEmail;

    @Value("${admin.password:admin}")
    private String adminPassword;

    @PostConstruct
    public void init() {
        for (UserType.PredefinedType predefinedType : UserType.PredefinedType.values()) {
            if (userTypeRepo.findByName(predefinedType.getName()) == null) {
                userTypeRepo.save(new UserType(predefinedType));
            }
        }

        for (CampaignCategory.PredefinedCategories predefinedCategory : CampaignCategory.PredefinedCategories
                .values()) {
            if (campaignCategoryRepo.findByName(predefinedCategory.name()) == null) {
                campaignCategoryRepo.save(new CampaignCategory(predefinedCategory));
            }
        }

        if (userRepo.findByUsername(adminUsername) != null) {
            return;
        }
        User adminUser = new User();
        adminUser.setUsername(adminUsername);
        adminUser.setEmail(adminEmail);
        adminUser.setPassword(UserUtil.hash(adminPassword));
        adminUser.setLoginStrategy("basic");
        adminUser.setUserType(userTypeRepo.findByName("Admin"));
        userRepo.save(adminUser);
    }
}
