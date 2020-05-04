package com.ft.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SmsCampaignMapperTest {

    private SmsCampaignMapper smsCampaignMapper;

    @BeforeEach
    public void setUp() {
        smsCampaignMapper = new SmsCampaignMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(smsCampaignMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(smsCampaignMapper.fromId(null)).isNull();
    }
}
