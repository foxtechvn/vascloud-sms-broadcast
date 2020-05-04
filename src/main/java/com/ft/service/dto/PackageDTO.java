package com.ft.service.dto;

public class PackageDTO {

	private String id;
	
	private String name;
	
	private String code;

	@Override
	public String toString() {
		return "PackageDTO [id=" + id + ", name=" + name + ", code=" + code + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

		
}
