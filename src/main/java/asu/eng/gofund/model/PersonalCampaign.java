package asu.eng.gofund.model;

import asu.eng.gofund.util.DatabaseUtil;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "personalCampaign")
public class PersonalCampaign extends Campaign {

    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    private Date startDate;
    private Date endDate;
    private Long targetAmount;
    private Long currentAmount;

    public PersonalCampaign() {
        super();
    }


    public PersonalCampaign(boolean isDeleted, String name, String description, String imageUrl,
                            CampaignStatus status, Currency currency, Long category, Long starterId,
                            String bankAccountNumber, Date startDate, Date endDate,
                            Long targetAmount, Long currentAmount, List<Address> addresses, List<Comment> comments) {
        super(isDeleted, name, description, imageUrl, status, currency, category, starterId, bankAccountNumber, addresses, comments);
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
    }

    public static int deletePersonalCampaign(Long id) {
        String sql = "UPDATE personal_campaign " +
                "SET is_deleted = TRUE " +
                "WHERE id = ? AND is_deleted = FALSE";

        // Execute the update and check the affected rows
        int rowsAffected = DatabaseUtil.getConnection().update(sql, id);

        // If a row was updated, return 1; otherwise, return 0
        return rowsAffected > 0 ? 1 : 0;
    }

    public static PersonalCampaign getPersonalCampaignById(Long id) {
        String sql = "SELECT * FROM personal_campaign WHERE id = ? AND is_deleted = FALSE";
        try {
            return DatabaseUtil.getConnection().queryForObject(sql,
                    new BeanPropertyRowMapper<>(PersonalCampaign.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null; // or throw a custom exception, or log the error as needed
        }
    }

    public static List<PersonalCampaign> getAllPersonalCampaigns() {
        String sql = "SELECT * FROM personal_campaign WHERE is_deleted = FALSE";
        return DatabaseUtil.getConnection().query(sql, new BeanPropertyRowMapper<>(PersonalCampaign.class));
    }


    public boolean updateCampaign(Long id, String name, String description, String imageUrl,
                                  Long status, Long currency, Long category, Long starterId,
                                  String bankAccountNumber, Date startDate, Date endDate,
                                  Long targetAmount, Long currentAmount) {
        return true;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Long targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Long getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Long currentAmount) {
        this.currentAmount = currentAmount;
    }

    public int savePersonalCampaign() {

        String sql = "INSERT INTO personal_campaign (is_deleted, name, description, image_url, status, currency, category, starter_id, " +
                "bank_account_number, start_date, end_date, target_amount, current_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return DatabaseUtil.getConnection().update(sql, this.isDeleted(), this.getName(), this.getDescription(),
                this.getImageUrl(), this.getStatus().getValue(), this.getCurrency().getValue(), this.getCategory().getValue(), this.getStarterId(),
                this.getBankAccountNumber(), getStartDate(), getEndDate(), getTargetAmount(), getCurrentAmount());


    }


    public static int updatePersonalCampaign(PersonalCampaign updatedCampaign) {
        // Check if the campaign exists and is not deleted
        PersonalCampaign existingCampaign = getPersonalCampaignById(updatedCampaign.getId());
        if (existingCampaign == null) {
            return 0; // Campaign not found or is deleted
        }

        // Prepare SQL for updating the campaign, excluding startDate and starterId
        String sql = "UPDATE personal_campaign SET " +
                "name = ?, description = ?, image_url = ?, status = ?, currency = ?, " +
                "category = ?, bank_account_number = ?, end_date = ?, " +
                "target_amount = ?, current_amount = ? " +
                "WHERE id = ? AND is_deleted = FALSE";

        // Execute update
        return DatabaseUtil.getConnection().update(sql,
                updatedCampaign.getName(), updatedCampaign.getDescription(),
                updatedCampaign.getImageUrl(), updatedCampaign.getStatus().getValue(),
                updatedCampaign.getCurrency().getValue(), updatedCampaign.getCategory().getValue(),
                updatedCampaign.getBankAccountNumber(),
                updatedCampaign.getEndDate(),
                updatedCampaign.getTargetAmount(), updatedCampaign.getCurrentAmount(),
                updatedCampaign.getId());
    }

    public int savePersonalCampaignWithAddresses(List<Address> addresses) {
        // First save the campaign
        String campaignSql = "INSERT INTO personal_campaign (is_deleted, name, description, image_url, status, currency, " +
                "category, starter_id, bank_account_number, start_date, end_date, target_amount, current_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        // Execute insert and get the generated campaign ID
        Long campaignId = DatabaseUtil.getConnection().queryForObject(campaignSql,
                Long.class,
                this.isDeleted(), this.getName(), this.getDescription(),
                this.getImageUrl(), this.getStatus().getValue(), this.getCurrency().getValue(),
                this.getCategory().getValue(), this.getStarterId(), this.getBankAccountNumber(),
                getStartDate(), getEndDate(), getTargetAmount(), getCurrentAmount());

        if (campaignId == null) {
            return 0; // Campaign creation failed
        }

        // Now save the address associations in the join table
        if (addresses != null && !addresses.isEmpty()) {
            String addressLinkSql = "INSERT INTO address_campaign (campaign_id, address_id) VALUES (?, ?)";

            for (Address address : addresses) {
                // If the address is new, save it first
                if (address.getId() == null) {
                    address.saveAddress();
                }

                // Link the address to the campaign
                DatabaseUtil.getConnection().update(addressLinkSql, campaignId, address.getId());
            }
        }

        // Set the generated ID to the current instance
        this.setId(campaignId);

        return 1; // Success
    }
}
