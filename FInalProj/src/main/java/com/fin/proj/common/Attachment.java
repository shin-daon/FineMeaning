package com.fin.proj.common;

public class Attachment {
	private int attmNo;
	private int refBoardNo;
	private String attmUrl;
	private String attmName;
	private String attmReName;
	private String attmText;
	private int attmClassify;
	private int attmLevel; // 썸네일용!
	
	public Attachment() {}
	
	public Attachment(int attmNo, int refBoardNo, String attmUrl, String attmName, String attmReName, String attmText,
			int attmClassify, int attmLevel) {
		super();
		this.attmNo = attmNo;
		this.refBoardNo = refBoardNo;
		this.attmUrl = attmUrl;
		this.attmName = attmName;
		this.attmReName = attmReName;
		this.attmText = attmText;
		this.attmClassify = attmClassify;
		this.attmLevel = attmLevel;
	}

	public int getAttmNo() {
		return attmNo;
	}
	public void setAttmNo(int attmNo) {
		this.attmNo = attmNo;
	}
	public int getRefBoardNo() {
		return refBoardNo;
	}
	public void setRefBoardNo(int refBoardNo) {
		this.refBoardNo = refBoardNo;
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
	public String getAttmReName() {
		return attmReName;
	}
	public void setAttmReName(String attmReName) {
		this.attmReName = attmReName;
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

	public int getAttmLevel() {
		return attmLevel;
	}

	public void setAttmLevel(int attmLevel) {
		this.attmLevel = attmLevel;
	}

	@Override
	public String toString() {
		return "Attachment [attmNo=" + attmNo + ", refBoardNo=" + refBoardNo + ", attmUrl=" + attmUrl + ", attmName="
				+ attmName + ", attmReName=" + attmReName + ", attmText=" + attmText + ", attmClassify=" + attmClassify
				+ ", attmLevel=" + attmLevel + "]";
	}
	
}
