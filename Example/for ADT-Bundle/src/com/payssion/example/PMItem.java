package com.payssion.example;

public class PMItem {
	private int mItemId;
	private String mPMId;
	private String mPMName;
	private String mCurrency;
	public int getItemId() {
		return mItemId;
	}
	public PMItem setItemId(int itemId) {
		this.mItemId = itemId;
		return this;
	}
	
	public String getPMId() {
		return mPMId;
	}
	public PMItem setPMId(String pmId) {
		this.mPMId = pmId;
		return this;
	}
	
	public String getPMName() {
		return mPMName;
	}
	public PMItem setPMName(String pmName) {
		this.mPMName = pmName;
		return this;
	}
	
	public String getCurrency() {
		return mCurrency;
	}
	public PMItem setCurrency(String currency) {
		this.mCurrency = currency;
		return this;
	}
}
