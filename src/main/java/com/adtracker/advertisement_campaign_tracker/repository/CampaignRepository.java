package com.adtracker.advertisement_campaign_tracker.repository;

import com.adtracker.advertisement_campaign_tracker.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    List<Campaign> findByCampaignNameContainingIgnoreCase(String campaignName);

    List<Campaign> findByAdvertiserContainingIgnoreCase(String advertiser);

    List<Campaign> findByPlatformContainingIgnoreCase(String platform);

    List<Campaign> findByStatusIgnoreCase(String status);
}