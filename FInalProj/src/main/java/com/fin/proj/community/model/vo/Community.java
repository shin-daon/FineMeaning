package com.fin.proj.community.model.vo;

import java.sql.Date;

public class Community {
	
	private int boardNo;
	private int writerNo;
	private int boardType;
	private String boardTitle;
	private String boardContent;
	private Date boardCreateDate;
	private char boardStatus;
	
	public Community() {}
	
	public Community(int boardNo, int writerNo, int boardType, String boardTitle, String boardContent,
			Date boardCreateDate, char boardStatus) {
		super();
		this.boardNo = boardNo;
		this.writerNo = writerNo;
		this.boardType = boardType;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.boardCreateDate = boardCreateDate;
		this.boardStatus = boardStatus;
	}

	public int getBoardNo() {
		return boardNo;
	}

	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}

	public int getWriterNo() {
		return writerNo;
	}

	public void setWriterNo(int writerNo) {
		this.writerNo = writerNo;
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

	public Date getBoardCreateDate() {
		return boardCreateDate;
	}

	public void setBoardCreateDate(Date boardCreateDate) {
		this.boardCreateDate = boardCreateDate;
	}

	public char getBoardStatus() {
		return boardStatus;
	}

	public void setBoardStatus(char boardStatus) {
		this.boardStatus = boardStatus;
	}

	@Override
	public String toString() {
		return "Community [boardNo=" + boardNo + ", writerNo=" + writerNo + ", boardType=" + boardType + ", boardTitle="
				+ boardTitle + ", boardContent=" + boardContent + ", boardCreateDate=" + boardCreateDate
				+ ", boardStatus=" + boardStatus + "]";
	}

	

}
