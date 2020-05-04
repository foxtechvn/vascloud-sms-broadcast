package com.ft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;

/**
 * A SmsCampaign.
 */
@Entity
@Table(name = "sms_campaign")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SmsCampaign implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "state", nullable = false)
    private Integer state;

    @Column(name = "short_msg")
    private String shortMsg;

    @Column(name = "start_at")
    private Instant startAt;

    @Column(name = "expired_at")
    private Instant expiredAt;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "pdu")
    private String pdu;

    @Column(name = "vas_cloud_package")
    private String vasCloudPackage;

    @Column(name = "rate_limit")
    private Long rateLimit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public SmsCampaign name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public SmsCampaign state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getShortMsg() {
        return shortMsg;
    }

    public SmsCampaign shortMsg(String shortMsg) {
        this.shortMsg = shortMsg;
        return this;
    }

    public void setShortMsg(String shortMsg) {
        this.shortMsg = shortMsg;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public SmsCampaign startAt(Instant startAt) {
        this.startAt = startAt;
        return this;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public SmsCampaign expiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
        return this;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getPdu() {
        return pdu;
    }

    public SmsCampaign pdu(String pdu) {
        this.pdu = pdu;
        return this;
    }

    public void setPdu(String pdu) {
        this.pdu = pdu;
    }

    public String getVasCloudPackage() {
        return vasCloudPackage;
    }

    public SmsCampaign vasCloudPackage(String vasCloudPackage) {
        this.vasCloudPackage = vasCloudPackage;
        return this;
    }

    public void setVasCloudPackage(String vasCloudPackage) {
        this.vasCloudPackage = vasCloudPackage;
    }

    public Long getRateLimit() {
        return rateLimit;
    }

    public SmsCampaign rateLimit(Long rateLimit) {
        this.rateLimit = rateLimit;
        return this;
    }

    public void setRateLimit(Long rateLimit) {
        this.rateLimit = rateLimit;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsCampaign)) {
            return false;
        }
        return id != null && id.equals(((SmsCampaign) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SmsCampaign{" +
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
