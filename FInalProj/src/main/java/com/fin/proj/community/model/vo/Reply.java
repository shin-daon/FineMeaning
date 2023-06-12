package com.fin.proj.community.model.vo;

import java.sql.Date;

public class Reply {
	
	private int replyNo;
	private int writerNo;
	private int boardNo;
	private String replyContent;
	private Date replyCreateDate;
	private char replyStatus;
	private int replyType;
	
	public Reply () {}

	public Reply(int replyNo, int writerNo, int boardNo, String replyContent, Date replyCreateDate, char replyStatus,
			int replyType) {
		super();
		this.replyNo = replyNo;
		this.writerNo = writerNo;
		this.boardNo = boardNo;
		this.replyContent = replyContent;
		this.replyCreateDate = replyCreateDate;
		this.replyStatus = replyStatus;
		this.replyType = replyType;
	}

	public int getReplyNo() {
		return replyNo;
	}

	public void setReplyNo(int replyNo) {
		this.replyNo = replyNo;
	}

	public int getWriterNo() {
		return writerNo;
	}

	public void setWriterNo(int writerNo) {
		this.writerNo = writerNo;
	}

	public int getBoardNo() {
		return boardNo;
	}

	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public Date getReplyCreateDate() {
		return replyCreateDate;
	}

	public void setReplyCreateDate(Date replyCreateDate) {
		this.replyCreateDate = replyCreateDate;
	}

	public char getReplyStatus() {
		return replyStatus;
	}

	public void setReplyStatus(char replyStatus) {
		this.replyStatus = replyStatus;
	}

	public int getReplyType() {
		return replyType;
	}

	public void setReplyType(int replyType) {
		this.replyType = replyType;
	}

	@Override
	public String toString() {
		return "Reply [replyNo=" + replyNo + ", writerNo=" + writerNo + ", boardNo=" + boardNo + ", replyContent="
				+ replyContent + ", replyCreateDate=" + replyCreateDate + ", replyStatus=" + replyStatus
				+ ", replyType=" + replyType + "]";
	}

	

}
