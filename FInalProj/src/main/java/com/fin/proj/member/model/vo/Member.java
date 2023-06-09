package com.fin.proj.member.model.vo;

import java.sql.Date;

public class Member {
	private int uNo;
	private String uId;
	private String uPwd;
	private String uName;
	private String residentNo;
	private String address;
	private int phone;
	private String email;
	private String uNickName;
	private String loginType;
	private int kakaoId;
	private char uStatus;
	private int uType;
	private String registrar;
	private Date enrollDate;
	
	public Member(int uNo, String uId, String uPwd, String uName, String residentNo, String address, int phone,
			String email, String uNickName, String loginType, int kakaoId, char uStatus, int uType, String registrar,
			Date enrollDate) {
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
	}

	public int getuNo() {
		return uNo;
	}

	public void setuNo(int uNo) {
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

	public int getKakaoId() {
		return kakaoId;
	}

	public void setKakaoId(int kakaoId) {
		this.kakaoId = kakaoId;
	}

	public char getuStatus() {
		return uStatus;
	}

	public void setuStatus(char uStatus) {
		this.uStatus = uStatus;
	}

	public int getuType() {
		return uType;
	}

	public void setuType(int uType) {
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

	@Override
	public String toString() {
		return "Member [uNo=" + uNo + ", uId=" + uId + ", uPwd=" + uPwd + ", uName=" + uName + ", residentNo="
				+ residentNo + ", address=" + address + ", phone=" + phone + ", email=" + email + ", uNickName="
				+ uNickName + ", loginType=" + loginType + ", kakaoId=" + kakaoId + ", uStatus=" + uStatus + ", uType="
				+ uType + ", registrar=" + registrar + ", enrollDate=" + enrollDate + "]";
	}
		
}
