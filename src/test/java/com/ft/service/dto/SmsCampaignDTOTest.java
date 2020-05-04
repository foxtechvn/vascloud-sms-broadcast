package com.ft.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ft.web.rest.TestUtil;

public class SmsCampaignDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsCampaignDTO.class);
        SmsCampaignDTO smsCampaignDTO1 = new SmsCampaignDTO();
        smsCampaignDTO1.setId(1L);
        SmsCampaignDTO smsCampaignDTO2 = new SmsCampaignDTO();
        assertThat(smsCampaignDTO1).isNotEqualTo(smsCampaignDTO2);
        smsCampaignDTO2.setId(smsCampaignDTO1.getId());
        assertThat(smsCampaignDTO1).isEqualTo(smsCampaignDTO2);
        smsCampaignDTO2.setId(2L);
        assertThat(smsCampaignDTO1).isNotEqualTo(smsCampaignDTO2);
        smsCampaignDTO1.setId(null);
        assertThat(smsCampaignDTO1).isNotEqualTo(smsCampaignDTO2);
    }
}
