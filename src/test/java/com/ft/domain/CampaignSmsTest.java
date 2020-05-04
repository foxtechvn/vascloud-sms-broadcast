package com.ft.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ft.web.rest.TestUtil;

public class CampaignSmsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CampaignSms.class);
        CampaignSms campaignSms1 = new CampaignSms();
        campaignSms1.setId(1L);
        CampaignSms campaignSms2 = new CampaignSms();
        campaignSms2.setId(campaignSms1.getId());
        assertThat(campaignSms1).isEqualTo(campaignSms2);
        campaignSms2.setId(2L);
        assertThat(campaignSms1).isNotEqualTo(campaignSms2);
        campaignSms1.setId(null);
        assertThat(campaignSms1).isNotEqualTo(campaignSms2);
    }
}
