package com.fin.proj.board.model.vo;

import java.sql.Date;

public class Board {
	private int boardNo;
	private int userNo;
	private String userId;
	private String nickName;
	private int boardType;
	private String boardTitle;
	private String boardContent;
	private Date boardDate;
	private char boardStatus;
	private int boardCount;
	private String newsURL;
	private String fpName;
	private String boardCate;
	
	public Board() {}

	public Board(int boardNo, int userNo, String userId, String nickName, int boardType, String boardTitle,
			String boardContent, Date boardDate, char boardStatus, int boardCount, String newsURL, String fpName,
			String boardCate) {
		super();
		this.boardNo = boardNo;
		this.userNo = userNo;
		this.userId = userId;
		this.nickName = nickName;
		this.boardType = boardType;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.boardDate = boardDate;
		this.boardStatus = boardStatus;
		this.boardCount = boardCount;
		this.newsURL = newsURL;
		this.fpName = fpName;
		this.boardCate = boardCate;
	}

	public int getBoardNo() {
		return boardNo;
	}

	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getBoardType() {
		return boardType;
	}

	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}

	public String getBoardTitle() {
		return boardTitle;
	}

	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}

	public String getBoardContent() {
		return boardContent;
	}

	public void setBoardContent(String boardContent) {
		this.boardContent = boardContent;
	}

	public Date getBoardDate() {
		return boardDate;
	}

	public void setBoardDate(Date boardDate) {
		this.boardDate = boardDate;
	}

	public char getBoardStatus() {
		return boardStatus;
	}

	public void setBoardStatus(char boardStatus) {
		this.boardStatus = boardStatus;
	}

	public int getBoardCount() {
		return boardCount;
	}

	public void setBoardCount(int boardCount) {
		this.boardCount = boardCount;
	}

	public String getNewsURL() {
		return newsURL;
	}

	public void setNewsURL(String newsURL) {
		this.newsURL = newsURL;
	}

	public String getFpName() {
		return fpName;
	}

	public void setFpName(String fpName) {
		this.fpName = fpName;
	}

	public String getBoardCate() {
		return boardCate;
	}

	public void setBoardCate(String boardCate) {
		this.boardCate = boardCate;
	}

	@Override
	public String toString() {
		return "Board [boardNo=" + boardNo + ", userNo=" + userNo + ", userId=" + userId + ", nickName=" + nickName
				+ ", boardType=" + boardType + ", boardTitle=" + boardTitle + ", boardContent=" + boardContent
				+ ", boardDate=" + boardDate + ", boardStatus=" + boardStatus + ", boardCount=" + boardCount
				+ ", newsURL=" + newsURL + ", fpName=" + fpName + ", boardCate=" + boardCate + "]";
	}
	
}
