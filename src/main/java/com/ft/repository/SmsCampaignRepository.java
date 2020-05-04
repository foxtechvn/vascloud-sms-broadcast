package com.ft.repository;

import com.ft.domain.SmsCampaign;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SmsCampaign entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsCampaignRepository extends JpaRepository<SmsCampaign, Long> {
}
