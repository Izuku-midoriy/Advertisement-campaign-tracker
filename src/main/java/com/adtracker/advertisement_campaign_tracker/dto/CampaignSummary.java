package com.adtracker.advertisement_campaign_tracker.dto;

public class CampaignSummary {

    private long totalCampaigns;
    private long activeCampaigns;
    private long completedCampaigns;
    private long pausedCampaigns;

    private double totalBudget;

    private long totalImpressions;
    private long totalClicks;
    private long totalConversions;

    public CampaignSummary() {
    }

    public CampaignSummary(
            long totalCampaigns,
            long activeCampaigns,
            long completedCampaigns,
            long pausedCampaigns,
            double totalBudget,
            long totalImpressions,
            long totalClicks,
            long totalConversions) {

        this.totalCampaigns = totalCampaigns;
        this.activeCampaigns = activeCampaigns;
        this.completedCampaigns = completedCampaigns;
        this.pausedCampaigns = pausedCampaigns;
        this.totalBudget = totalBudget;
        this.totalImpressions = totalImpressions;
        this.totalClicks = totalClicks;
        this.totalConversions = totalConversions;
    }

    public long getTotalCampaigns() {
        return totalCampaigns;
    }

    public void setTotalCampaigns(long totalCampaigns) {
        this.totalCampaigns = totalCampaigns;
    }

    public long getActiveCampaigns() {
        return activeCampaigns;
    }

    public void setActiveCampaigns(long activeCampaigns) {
        this.activeCampaigns = activeCampaigns;
    }

    public long getCompletedCampaigns() {
        return completedCampaigns;
    }

    public void setCompletedCampaigns(long completedCampaigns) {
        this.completedCampaigns = completedCampaigns;
    }

    public long getPausedCampaigns() {
        return pausedCampaigns;
    }

    public void setPausedCampaigns(long pausedCampaigns) {
        this.pausedCampaigns = pausedCampaigns;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public long getTotalImpressions() {
        return totalImpressions;
    }

    public void setTotalImpressions(long totalImpressions) {
        this.totalImpressions = totalImpressions;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(long totalClicks) {
        this.totalClicks = totalClicks;
    }

    public long getTotalConversions() {
        return totalConversions;
    }

    public void setTotalConversions(long totalConversions) {
        this.totalConversions = totalConversions;
    }
}