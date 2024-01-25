package com.fin.proj.user.model.vo;

import java.sql.Date;
import java.sql.Timestamp;

public class User {
	private Integer uNo;
	private String uId;
	private String uPwd;
	private String uName;
	private String residentNo;
	private String address;
	private String phone;
	private String email;
	private String uNickName;
	private String loginType;
	private Long kakaoId;
	private String uStatus;
	private String uType;
	private String registrar;
	private Date enrollDate;
	private Integer isAdmin;
	
	public User() {}

	public User(Integer uNo, String uId, String uPwd, String uName, String residentNo, String address, String phone,
			String email, String uNickName, String loginType, Long kakaoId, String uStatus, String uType,
			String registrar, Date enrollDate, Integer isAdmin) {
		super();
		this.uNo = uNo;
		this.uId = uId;
		this.uPwd = uPwd;
		this.uName = uName;
		this.residentNo = residentNo;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.uNickName = uNickName;
		this.loginType = loginType;
		this.kakaoId = kakaoId;
		this.uStatus = uStatus;
		this.uType = uType;
		this.registrar = registrar;
		this.enrollDate = enrollDate;
		this.isAdmin = isAdmin;
	}

	public Integer getuNo() {
		return uNo;
	}

	public void setuNo(Integer uNo) {
		this.uNo = uNo;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getuPwd() {
		return uPwd;
	}

	public void setuPwd(String uPwd) {
		this.uPwd = uPwd;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getResidentNo() {
		return residentNo;
	}

	public void setResidentNo(String residentNo) {
		this.residentNo = residentNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getuNickName() {
		return uNickName;
	}

	public void setuNickName(String uNickName) {
		this.uNickName = uNickName;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public Long getKakaoId() {
		return kakaoId;
	}

	public void setKakaoId(Long kakaoId) {
		this.kakaoId = kakaoId;
	}

	public String getuStatus() {
		return uStatus;
	}

	public void setuStatus(String uStatus) {
		this.uStatus = uStatus;
	}

	public String getuType() {
		return uType;
	}

	public void setuType(String uType) {
		this.uType = uType;
	}

	public String getRegistrar() {
		return registrar;
	}

	public void setRegistrar(String registrar) {
		this.registrar = registrar;
	}

	public Date getEnrollDate() {
		return enrollDate;
	}

	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}

	public Integer getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return "User [uNo=" + uNo + ", uId=" + uId + ", uPwd=" + uPwd + ", uName=" + uName + ", residentNo="
				+ residentNo + ", address=" + address + ", phone=" + phone + ", email=" + email + ", uNickName="
				+ uNickName + ", loginType=" + loginType + ", kakaoId=" + kakaoId + ", uStatus=" + uStatus + ", uType="
				+ uType + ", registrar=" + registrar + ", enrollDate=" + enrollDate + ", isAdmin=" + isAdmin + "]";
	}
	
}
