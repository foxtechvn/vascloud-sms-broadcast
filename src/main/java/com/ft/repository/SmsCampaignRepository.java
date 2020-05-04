package com.ft.repository;

import com.ft.domain.QSmsCampaign;
import com.ft.domain.SmsCampaign;

import java.time.Instant;
import java.util.List;

import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.ExpressionProviderFactory;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SmsCampaign entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsCampaignRepository extends JpaRepository<SmsCampaign, Long>, QuerydslPredicateExecutor<SmsCampaign>, QuerydslBinderCustomizer<QSmsCampaign> {

	// Support for browsing campaign in web
	@Override
	default public void customize(QuerydslBindings bindings, QSmsCampaign root) {
		bindings.bind(root.id).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
	}

	public List<SmsCampaign> findAllByState(int state);

	public List<SmsCampaign> findAllByStartAtGreaterThanAndState(Instant today, int state);

	public List<SmsCampaign> findAllByExpiredAtLessThanAndState(Instant now, int value);

	public List<SmsCampaign> findAllByStartAtLessThanAndState(Instant now, int value);

	public List<SmsCampaign> findAllByStartAtLessThanAndStateNotIn(Instant minusSeconds, List<Integer> asList);

	public List<SmsCampaign> findAllByExpiredAtGreaterThan(Instant now);
}
