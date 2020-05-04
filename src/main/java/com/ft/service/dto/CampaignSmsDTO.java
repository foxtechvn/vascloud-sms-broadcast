package com.ft.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ft.domain.CampaignSms} entity.
 */
public class CampaignSmsDTO implements Serializable {
    
    private Long id;

    private String msisdn;

    private String text;

    private Integer state;

    private Instant submitAt;

    private Instant expiredAt;

    private Instant requestAt;


    private Long campaignIdId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Instant getSubmitAt() {
        return submitAt;
    }

    public void setSubmitAt(Instant submitAt) {
        this.submitAt = submitAt;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Instant getRequestAt() {
        return requestAt;
    }

    public void setRequestAt(Instant requestAt) {
        this.requestAt = requestAt;
    }

    public Long getCampaignIdId() {
        return campaignIdId;
    }

    public void setCampaignIdId(Long smsCampaignId) {
        this.campaignIdId = smsCampaignId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CampaignSmsDTO campaignSmsDTO = (CampaignSmsDTO) o;
        if (campaignSmsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), campaignSmsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CampaignSmsDTO{" +
            "id=" + getId() +
            ", msisdn='" + getMsisdn() + "'" +
            ", text='" + getText() + "'" +
            ", state=" + getState() +
            ", submitAt='" + getSubmitAt() + "'" +
            ", expiredAt='" + getExpiredAt() + "'" +
            ", requestAt='" + getRequestAt() + "'" +
            ", campaignIdId=" + getCampaignIdId() +
            "}";
    }
}
