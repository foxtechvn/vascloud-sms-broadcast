package com.ft.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ft.web.rest.TestUtil;

public class SmsCampaignTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsCampaign.class);
        SmsCampaign smsCampaign1 = new SmsCampaign();
        smsCampaign1.setId(1L);
        SmsCampaign smsCampaign2 = new SmsCampaign();
        smsCampaign2.setId(smsCampaign1.getId());
        assertThat(smsCampaign1).isEqualTo(smsCampaign2);
        smsCampaign2.setId(2L);
        assertThat(smsCampaign1).isNotEqualTo(smsCampaign2);
        smsCampaign1.setId(null);
        assertThat(smsCampaign1).isNotEqualTo(smsCampaign2);
    }
}
