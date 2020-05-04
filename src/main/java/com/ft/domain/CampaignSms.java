package com.ft.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A CampaignSms.
 */
@Entity
@Table(name = "campaign_sms")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CampaignSms implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "msisdn")
    private String msisdn;

    @Column(name = "text")
    private String text;

    @Column(name = "state")
    private Integer state;

    @Column(name = "request_at")
    private Instant requestAt;

    @ManyToOne
    @JsonIgnoreProperties("campaignSms")
    private SmsCampaign campaign;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public CampaignSms msisdn(String msisdn) {
        this.msisdn = msisdn;
        return this;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getText() {
        return text;
    }

    public CampaignSms text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getState() {
        return state;
    }

    public CampaignSms state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Instant getRequestAt() {
        return requestAt;
    }

    public CampaignSms requestAt(Instant requestAt) {
        this.requestAt = requestAt;
        return this;
    }

    public void setRequestAt(Instant requestAt) {
        this.requestAt = requestAt;
    }

    public SmsCampaign getCampaign() {
        return campaign;
    }

    public CampaignSms campaign(SmsCampaign smsCampaign) {
        this.campaign = smsCampaign;
        return this;
    }

    public void setCampaign(SmsCampaign smsCampaign) {
        this.campaign = smsCampaign;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CampaignSms)) {
            return false;
        }
        return id != null && id.equals(((CampaignSms) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CampaignSms{" +
            "id=" + getId() +
            ", msisdn='" + getMsisdn() + "'" +
            ", text='" + getText() + "'" +
            ", state=" + getState() +
            ", requestAt='" + getRequestAt() + "'" +
            "}";
    }
}
