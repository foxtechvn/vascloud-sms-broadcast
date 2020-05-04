package com.ft.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ft.web.rest.TestUtil;

public class CampaignSmsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CampaignSmsDTO.class);
        CampaignSmsDTO campaignSmsDTO1 = new CampaignSmsDTO();
        campaignSmsDTO1.setId(1L);
        CampaignSmsDTO campaignSmsDTO2 = new CampaignSmsDTO();
        assertThat(campaignSmsDTO1).isNotEqualTo(campaignSmsDTO2);
        campaignSmsDTO2.setId(campaignSmsDTO1.getId());
        assertThat(campaignSmsDTO1).isEqualTo(campaignSmsDTO2);
        campaignSmsDTO2.setId(2L);
        assertThat(campaignSmsDTO1).isNotEqualTo(campaignSmsDTO2);
        campaignSmsDTO1.setId(null);
        assertThat(campaignSmsDTO1).isNotEqualTo(campaignSmsDTO2);
    }
}
