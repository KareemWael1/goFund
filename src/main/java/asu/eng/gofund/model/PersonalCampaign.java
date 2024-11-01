package asu.eng.gofund.model;

import asu.eng.gofund.util.DatabaseUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "personalCampaign")
public class PersonalCampaign extends Campaign {
    private Date startDate;
    private Date endDate;
    private Long targetAmount;
    private Long currentAmount;

    public PersonalCampaign() {
        super();
    }

    public PersonalCampaign(Long id, String name, String description, String imageUrl,
                            CampaignStatus status, Currency currency, Long category, Long starterId,
                            String bankAccountNumber, List<Address> addresses, Date startDate, Date endDate,
                            Long targetAmount, Long currentAmount) {
        super(id, name, description, imageUrl, status, currency, category, starterId, bankAccountNumber, addresses);
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
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

    public int saveCampaign(Campaign campaign) {
        String sql = "INSERT INTO personalCampaign (name, description, image_url, status, currency, category, starter_id, \n" +
                "bank_account_number,addresses, start_date, end_date, target_amount, current_amount)\n" +
                "VALUES  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\n";
        return DatabaseUtil.jdbcTemplate.update(sql, this.getName(), this.getDescription(),
                this.getImageUrl(), this.getStatus(), this.getCurrency(), this.getCategory(), this.getStarterId(),
                this.getBankAccountNumber(), getAddresses(), getStartDate(), getEndDate(), getTargetAmount(), getCurrentAmount());

    }
}
