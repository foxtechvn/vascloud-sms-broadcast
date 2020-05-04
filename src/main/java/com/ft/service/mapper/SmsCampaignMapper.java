package com.ft.service.mapper;


import com.ft.domain.*;
import com.ft.service.dto.SmsCampaignDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SmsCampaign} and its DTO {@link SmsCampaignDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmsCampaignMapper extends EntityMapper<SmsCampaignDTO, SmsCampaign> {



    default SmsCampaign fromId(Long id) {
        if (id == null) {
            return null;
        }
        SmsCampaign smsCampaign = new SmsCampaign();
        smsCampaign.setId(id);
        return smsCampaign;
    }
}
