package com.ft.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.ft.domain.SmsCampaign} entity.
 */
public class SmsCampaignDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer state;

    private String shortMsg;

    private Instant startAt;

    private Instant expiredAt;

    @Lob
    private String pdu;

    private String vasCloudPackage;

    private Long rateLimit;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getShortMsg() {
        return shortMsg;
    }

    public void setShortMsg(String shortMsg) {
        this.shortMsg = shortMsg;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getPdu() {
        return pdu;
    }

    public void setPdu(String pdu) {
        this.pdu = pdu;
    }

    public String getVasCloudPackage() {
        return vasCloudPackage;
    }

    public void setVasCloudPackage(String vasCloudPackage) {
        this.vasCloudPackage = vasCloudPackage;
    }

    public Long getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(Long rateLimit) {
        this.rateLimit = rateLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SmsCampaignDTO smsCampaignDTO = (SmsCampaignDTO) o;
        if (smsCampaignDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smsCampaignDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmsCampaignDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", state=" + getState() +
            ", shortMsg='" + getShortMsg() + "'" +
            ", startAt='" + getStartAt() + "'" +
            ", expiredAt='" + getExpiredAt() + "'" +
            ", pdu='" + getPdu() + "'" +
            ", vasCloudPackage='" + getVasCloudPackage() + "'" +
            ", rateLimit=" + getRateLimit() +
            "}";
    }
}
