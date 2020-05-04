package com.ft.repository;

import com.ft.domain.CampaignSms;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CampaignSms entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CampaignSmsRepository extends JpaRepository<CampaignSms, Long> {
}
