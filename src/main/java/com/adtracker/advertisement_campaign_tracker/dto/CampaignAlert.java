package com.adtracker.advertisement_campaign_tracker.dto;

public class CampaignAlert {

    private Long campaignId;
    private String campaignName;
    private String alertType;
    private String message;
    private String severity;

    public CampaignAlert() {
    }

    public CampaignAlert(
            Long campaignId,
            String campaignName,
            String alertType,
            String message,
            String severity) {

        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.alertType = alertType;
        this.message = message;
        this.severity = severity;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}