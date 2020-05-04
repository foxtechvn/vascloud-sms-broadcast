package com.ft.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class ScheduleDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ZonedDateTime startAt;
	
	private ZonedDateTime expiredAt;
	
	private Integer status;

	@Override
	public String toString() {
		return "ScheduleDTO [startAt=" + startAt + ", expiredAt=" + expiredAt + ", status=" + status + "]";
	}

	public ZonedDateTime getStartAt() {
		return startAt;
	}

	public void setStartAt(ZonedDateTime startAt) {
		this.startAt = startAt;
	}

	public ZonedDateTime getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(ZonedDateTime expiredAt) {
		this.expiredAt = expiredAt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	

}
