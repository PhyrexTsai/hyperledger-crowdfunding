package me.phyrextsai.hyperledger.crowdfunding.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by phyrextsai on 2016/11/11.
 */
public class Campaign {

    private static Log log = LogFactory.getLog(Campaign.class);

    private String campaignId;
    private String owner;
    private String info;
    private Integer fundingAmount;
    private Long campaignDueDate;

    public Campaign(String campaignId, String owner, String info, Integer fundingAmount, Long campaignDueDate) {
        this.setCampaignId(campaignId);
        this.setOwner(owner);
        this.setInfo(info);
        this.setFundingAmount(fundingAmount);
        this.setCampaignDueDate(campaignDueDate);
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getFundingAmount() {
        return fundingAmount;
    }

    public void setFundingAmount(Integer fundingAmount) {
        this.fundingAmount = fundingAmount;
    }

    public Long getCampaignDueDate() {
        return campaignDueDate;
    }

    public void setCampaignDueDate(Long campaignDueDate) {
        this.campaignDueDate = campaignDueDate;
    }

}
