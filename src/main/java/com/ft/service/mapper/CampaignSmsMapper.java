package com.ft.service.mapper;


import com.ft.domain.*;
import com.ft.service.dto.CampaignSmsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CampaignSms} and its DTO {@link CampaignSmsDTO}.
 */
@Mapper(componentModel = "spring", uses = {SmsCampaignMapper.class})
public interface CampaignSmsMapper extends EntityMapper<CampaignSmsDTO, CampaignSms> {

    @Mapping(source = "campaignId.id", target = "campaignIdId")
    CampaignSmsDTO toDto(CampaignSms campaignSms);

    @Mapping(source = "campaignIdId", target = "campaignId")
    CampaignSms toEntity(CampaignSmsDTO campaignSmsDTO);

    default CampaignSms fromId(Long id) {
        if (id == null) {
            return null;
        }
        CampaignSms campaignSms = new CampaignSms();
        campaignSms.setId(id);
        return campaignSms;
    }
}
