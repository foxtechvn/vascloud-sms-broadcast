package com.ft.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CampaignSmsMapperTest {

    private CampaignSmsMapper campaignSmsMapper;

    @BeforeEach
    public void setUp() {
        campaignSmsMapper = new CampaignSmsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(campaignSmsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(campaignSmsMapper.fromId(null)).isNull();
    }
}
