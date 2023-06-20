package com.fin.proj.support.model.vo;

import java.sql.Date;

public class Support {
	private int supportNo;
	private int userNo;
	private String supportCategory;
	private String supportTitle;
	private String supportContent;
	private Date createDate;
	private Date startDate;
	private Date endDate;
	private int targetAmount;
	private int fundAmount;
	private int supportCount;
	private char status;
	private String imageUrl;
	
	private int phone;
	private String email;
	private String registar;
	private String userId;
	private Date enrollDate;
	
	
	public Support() {}


	public Support(int supportNo, int userNo, String supportCategory, String supportTitle, String supportContent,
			Date createDate, Date startDate, Date endDate, int targetAmount, int fundAmount, int supportCount,
			char status, String imageUrl, int phone, String email, String registar, String userId, Date enrollDate) {
		super();
		this.supportNo = supportNo;
		this.userNo = userNo;
		this.supportCategory = supportCategory;
		this.supportTitle = supportTitle;
		this.supportContent = supportContent;
		this.createDate = createDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.targetAmount = targetAmount;
		this.fundAmount = fundAmount;
		this.supportCount = supportCount;
		this.status = status;
		this.imageUrl = imageUrl;
		this.phone = phone;
		this.email = email;
		this.registar = registar;
		this.userId = userId;
		this.enrollDate = enrollDate;
	}


	public int getSupportNo() {
		return supportNo;
	}


	public void setSupportNo(int supportNo) {
		this.supportNo = supportNo;
	}


	public int getUserNo() {
		return userNo;
	}


	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}


	public String getSupportCategory() {
		return supportCategory;
	}


	public void setSupportCategory(String supportCategory) {
		this.supportCategory = supportCategory;
	}


	public String getSupportTitle() {
		return supportTitle;
	}


	public void setSupportTitle(String supportTitle) {
		this.supportTitle = supportTitle;
	}


	public String getSupportContent() {
		return supportContent;
	}


	public void setSupportContent(String supportContent) {
		this.supportContent = supportContent;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public int getTargetAmount() {
		return targetAmount;
	}


	public void setTargetAmount(int targetAmount) {
		this.targetAmount = targetAmount;
	}


	public int getFundAmount() {
		return fundAmount;
	}


	public void setFundAmount(int fundAmount) {
		this.fundAmount = fundAmount;
	}


	public int getSupportCount() {
		return supportCount;
	}


	public void setSupportCount(int supportCount) {
		this.supportCount = supportCount;
	}


	public char getStatus() {
		return status;
	}


	public void setStatus(char status) {
		this.status = status;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public int getPhone() {
		return phone;
	}


	public void setPhone(int phone) {
		this.phone = phone;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getRegistar() {
		return registar;
	}


	public void setRegistar(String registar) {
		this.registar = registar;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public Date getEnrollDate() {
		return enrollDate;
	}


	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}


	@Override
	public String toString() {
		return "Support [supportNo=" + supportNo + ", userNo=" + userNo + ", supportCategory=" + supportCategory
				+ ", supportTitle=" + supportTitle + ", supportContent=" + supportContent + ", createDate=" + createDate
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", targetAmount=" + targetAmount
				+ ", fundAmount=" + fundAmount + ", supportCount=" + supportCount + ", status=" + status + ", imageUrl="
				+ imageUrl + ", phone=" + phone + ", email=" + email + ", registar=" + registar + ", userId=" + userId
				+ ", enrollDate=" + enrollDate + "]";
	}


	
	
}
