package com.fin.proj.community.model.vo;

public class Attatchment {
	
	private int attmNo;
	private int boardNo;
	private String attmUrl;
	private String attmName;
	private String attmRename;
	private String attmText;
	private int attmClassify;
	
	public Attatchment() {}
	
	public Attatchment(int attmNo, int boardNo, String attmUrl, String attmName, String attmRename, String attmText,
			int attmClassify) {
		super();
		this.attmNo = attmNo;
		this.boardNo = boardNo;
		this.attmUrl = attmUrl;
		this.attmName = attmName;
		this.attmRename = attmRename;
		this.attmText = attmText;
		this.attmClassify = attmClassify;
	}
	public int getAttmNo() {
		return attmNo;
	}
	public void setAttmNo(int attmNo) {
		this.attmNo = attmNo;
	}
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	public String getAttmUrl() {
		return attmUrl;
	}
	public void setAttmUrl(String attmUrl) {
		this.attmUrl = attmUrl;
	}
	public String getAttmName() {
		return attmName;
	}
	public void setAttmName(String attmName) {
		this.attmName = attmName;
	}
	public String getAttmRename() {
		return attmRename;
	}
	public void setAttmRename(String attmRename) {
		this.attmRename = attmRename;
	}
	public String getAttmText() {
		return attmText;
	}
	public void setAttmText(String attmText) {
		this.attmText = attmText;
	}
	public int getAttmClassify() {
		return attmClassify;
	}
	public void setAttmClassify(int attmClassify) {
		this.attmClassify = attmClassify;
	}

	@Override
	public String toString() {
		return "Attatchment [attmNo=" + attmNo + ", boardNo=" + boardNo + ", attmUrl=" + attmUrl + ", attmName="
				+ attmName + ", attmRename=" + attmRename + ", attmText=" + attmText + ", attmClassify=" + attmClassify
				+ "]";
	}
	
}
