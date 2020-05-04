package com.ft.repository;

import com.ft.domain.CampaignSms;
import com.ft.domain.QCampaignSms;

import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.ExpressionProviderFactory;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CampaignSms entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CampaignSmsRepository extends JpaRepository<CampaignSms, Long>, QuerydslPredicateExecutor<CampaignSms>,  QuerydslBinderCustomizer<QCampaignSms> {

	// Support for browsing campaign in web
	@Override
	default public void customize(QuerydslBindings bindings, QCampaignSms root) {
		bindings.bind(root.id).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.msisdn).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.campaignId.id).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.campaignId.name).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
	}
}
