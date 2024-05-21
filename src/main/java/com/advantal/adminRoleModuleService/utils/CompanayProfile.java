package com.advantal.adminRoleModuleService.utils;

public class CompanayProfile {

	private String symbol;
	private String name;
	private String exchange;
	private String mic_code;
	private String sector;
	private String industry;
	private String employees;
	private String website;
	private String description;
	private String type;
	private String CEO;
	private String address;
	private String city;
	private String zip;
	private String state;
	private String country;
	private String phone;
	
	
	
	public CompanayProfile() {
		super();
	}

	public CompanayProfile(String symbol, String name, String exchange, String mic_code, String sector, String industry,
			String employees, String website, String description, String type, String cEO, String address, String city,
			String zip, String state, String country, String phone) {
		super();
		this.symbol = symbol;
		this.name = name;
		this.exchange = exchange;
		this.mic_code = mic_code;
		this.sector = sector;
		this.industry = industry;
		this.employees = employees;
		this.website = website;
		this.description = description;
		this.type = type;
		CEO = cEO;
		this.address = address;
		this.city = city;
		this.zip = zip;
		this.state = state;
		this.country = country;
		this.phone = phone;
	}

	public String getSymbol() {
		return symbol;
	}



	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getExchange() {
		return exchange;
	}



	public void setExchange(String exchange) {
		this.exchange = exchange;
	}



	public String getMic_code() {
		return mic_code;
	}



	public void setMic_code(String mic_code) {
		this.mic_code = mic_code;
	}



	public String getSector() {
		return sector;
	}



	public void setSector(String sector) {
		this.sector = sector;
	}



	public String getIndustry() {
		return industry;
	}



	public void setIndustry(String industry) {
		this.industry = industry;
	}



	public String getEmployees() {
		return employees;
	}



	public void setEmployees(String employees) {
		this.employees = employees;
	}



	public String getWebsite() {
		return website;
	}



	public void setWebsite(String website) {
		this.website = website;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getCEO() {
		return CEO;
	}



	public void setCEO(String cEO) {
		CEO = cEO;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public String getZip() {
		return zip;
	}



	public void setZip(String zip) {
		this.zip = zip;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public String getCountry() {
		return country;
	}



	public void setCountry(String country) {
		this.country = country;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	@Override
	public String toString() {
		return "CompanayProfile [symbol=" + symbol + ", name=" + name + ", exchange=" + exchange + ", mic_code="
				+ mic_code + ", sector=" + sector + ", industry=" + industry + ", employees=" + employees + ", website="
				+ website + ", description=" + description + ", type=" + type + ", CEO=" + CEO + ", address=" + address
				+ ", city=" + city + ", zip=" + zip + ", state=" + state + ", country=" + country + ", phone=" + phone
				+ "]";
	}
	
	
	
}
