package com.fin.proj.board.model.vo;

import java.sql.Date;

public class Board {
	private int boardNo;
	private int uNo;
	private String uId;
	private String nickName;
	private String boardType;
	private String boardTitle;
	private String boardContent;
	private Date boardDate;
	private char boardStatus;
	private int boardCount;
	private String newsURL;
	private String fpName;
	private int boardCate;
	private String imageUrl;
	
	public Board() {}

	public Board(int boardNo, int uNo, String uId, String nickName, String boardType, String boardTitle,
			String boardContent, Date boardDate, char boardStatus, int boardCount, String newsURL, String fpName,
			int boardCate, String imageUrl) {
		super();
		this.boardNo = boardNo;
		this.uNo = uNo;
		this.uId = uId;
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
		this.imageUrl = imageUrl;
	}

	public int getBoardNo() {
		return boardNo;
	}

	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getBoardType() {
		return boardType;
	}

	public void setBoardType(String boardType) {
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

	public int getBoardCate() {
		return boardCate;
	}

	public void setBoardCate(int boardCate) {
		this.boardCate = boardCate;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "Board [boardNo=" + boardNo + ", uNo=" + uNo + ", uId=" + uId + ", nickName=" + nickName + ", boardType="
				+ boardType + ", boardTitle=" + boardTitle + ", boardContent=" + boardContent + ", boardDate="
				+ boardDate + ", boardStatus=" + boardStatus + ", boardCount=" + boardCount + ", newsURL=" + newsURL
				+ ", fpName=" + fpName + ", boardCate=" + boardCate + ", imageUrl=" + imageUrl + "]";
	}

}
