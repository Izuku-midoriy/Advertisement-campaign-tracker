package com.adtracker.advertisement_campaign_tracker.controller;

import com.adtracker.advertisement_campaign_tracker.entity.Campaign;
import com.adtracker.advertisement_campaign_tracker.repository.CampaignRepository;
import org.springframework.web.bind.annotation.*;
import com.adtracker.advertisement_campaign_tracker.dto.CampaignAlert;
import com.adtracker.advertisement_campaign_tracker.dto.CampaignSummary;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignRepository campaignRepository;

    public CampaignController(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @GetMapping
    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    @GetMapping("/alerts")
public List<CampaignAlert> getCampaignAlerts() {

    List<Campaign> campaigns = campaignRepository.findAll();

    List<CampaignAlert> alerts = new ArrayList<>();

    LocalDate today = LocalDate.now();

    for (Campaign campaign : campaigns) {

        // Alert 1: Active campaign has already ended
        if ("ACTIVE".equalsIgnoreCase(campaign.getStatus())
                && campaign.getEndDate() != null
                && campaign.getEndDate().isBefore(today)) {

            alerts.add(new CampaignAlert(
                    campaign.getId(),
                    campaign.getCampaignName(),
                    "CAMPAIGN_ENDED",
                    "Campaign end date has passed but campaign is still ACTIVE.",
                    "HIGH"
            ));
        }

        // Alert 2: Clicks exist but conversions are zero
        if (campaign.getClicks() != null
                && campaign.getClicks() > 0
                && (campaign.getConversions() == null
                || campaign.getConversions() == 0)) {

            alerts.add(new CampaignAlert(
                    campaign.getId(),
                    campaign.getCampaignName(),
                    "ZERO_CONVERSIONS",
                    "Campaign has clicks but no conversions.",
                    "MEDIUM"
            ));
        }

        // Alert 3: Impressions exist but clicks are zero
        if (campaign.getImpressions() != null
                && campaign.getImpressions() > 0
                && (campaign.getClicks() == null
                || campaign.getClicks() == 0)) {

            alerts.add(new CampaignAlert(
                    campaign.getId(),
                    campaign.getCampaignName(),
                    "ZERO_CLICKS",
                    "Campaign has impressions but no clicks.",
                    "MEDIUM"
            ));
        }

        // Alert 4: Invalid budget
        if (campaign.getBudget() == null
                || campaign.getBudget() <= 0) {

            alerts.add(new CampaignAlert(
                    campaign.getId(),
                    campaign.getCampaignName(),
                    "INVALID_BUDGET",
                    "Campaign budget is missing or less than or equal to zero.",
                    "HIGH"
            ));
        }
    }

    return alerts;
}

    @GetMapping("/{id}")
    public Campaign getCampaignById(@PathVariable Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Campaign not found with id: " + id));
    }

    @PostMapping
    public Campaign createCampaign(@RequestBody Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    @PutMapping("/{id}")
    public Campaign updateCampaign(
            @PathVariable Long id,
            @RequestBody Campaign campaignDetails) {

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Campaign not found with id: " + id));

        campaign.setCampaignName(campaignDetails.getCampaignName());
        campaign.setAdvertiser(campaignDetails.getAdvertiser());
        campaign.setPlatform(campaignDetails.getPlatform());
        campaign.setBudget(campaignDetails.getBudget());
        campaign.setStartDate(campaignDetails.getStartDate());
        campaign.setEndDate(campaignDetails.getEndDate());
        campaign.setStatus(campaignDetails.getStatus());
        campaign.setImpressions(campaignDetails.getImpressions());
        campaign.setClicks(campaignDetails.getClicks());
        campaign.setConversions(campaignDetails.getConversions());

        return campaignRepository.save(campaign);
    }

    @DeleteMapping("/{id}")
    public String deleteCampaign(@PathVariable Long id) {

        if (!campaignRepository.existsById(id)) {
            throw new RuntimeException(
                    "Campaign not found with id: " + id);
        }

        campaignRepository.deleteById(id);

        return "Campaign deleted successfully";
    }

    @GetMapping("/search")
public List<Campaign> searchCampaigns(@RequestParam String keyword) {

    List<Campaign> results = campaignRepository
            .findByCampaignNameContainingIgnoreCase(keyword);

    results.addAll(
            campaignRepository
                    .findByAdvertiserContainingIgnoreCase(keyword)
    );

    results.addAll(
            campaignRepository
                    .findByPlatformContainingIgnoreCase(keyword)
    );

    return results.stream().distinct().toList();
}

@GetMapping("/status/{status}")
public List<Campaign> getCampaignsByStatus(
        @PathVariable String status) {

    return campaignRepository.findByStatusIgnoreCase(status);
}

@GetMapping("/summary")
public CampaignSummary getSummary() {

    List<Campaign> campaigns = campaignRepository.findAll();

    long totalCampaigns = campaigns.size();

    long activeCampaigns = campaigns.stream()
            .filter(c -> "ACTIVE".equalsIgnoreCase(c.getStatus()))
            .count();

    long completedCampaigns = campaigns.stream()
            .filter(c -> "COMPLETED".equalsIgnoreCase(c.getStatus()))
            .count();

    long pausedCampaigns = campaigns.stream()
            .filter(c -> "PAUSED".equalsIgnoreCase(c.getStatus()))
            .count();

    double totalBudget = campaigns.stream()
            .filter(c -> c.getBudget() != null)
            .mapToDouble(Campaign::getBudget)
            .sum();

    long totalImpressions = campaigns.stream()
            .filter(c -> c.getImpressions() != null)
            .mapToLong(Campaign::getImpressions)
            .sum();

    long totalClicks = campaigns.stream()
            .filter(c -> c.getClicks() != null)
            .mapToLong(Campaign::getClicks)
            .sum();

    long totalConversions = campaigns.stream()
            .filter(c -> c.getConversions() != null)
            .mapToLong(Campaign::getConversions)
            .sum();

    return new CampaignSummary(
            totalCampaigns,
            activeCampaigns,
            completedCampaigns,
            pausedCampaigns,
            totalBudget,
            totalImpressions,
            totalClicks,
            totalConversions
    );
}

@PutMapping("/{id}/metrics")
public Campaign updateMetrics(
        @PathVariable Long id,
        @RequestBody Map<String, Long> metrics) {

    Campaign campaign = campaignRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Campaign not found"));

    if (metrics.containsKey("impressions")) {
        campaign.setImpressions(metrics.get("impressions"));
    }

    if (metrics.containsKey("clicks")) {
        campaign.setClicks(metrics.get("clicks"));
    }

    if (metrics.containsKey("conversions")) {
        campaign.setConversions(metrics.get("conversions"));
    }

    return campaignRepository.save(campaign);
 }

}