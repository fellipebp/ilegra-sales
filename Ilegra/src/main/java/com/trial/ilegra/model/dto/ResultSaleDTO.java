package com.trial.ilegra.model.dto;

import java.io.Serializable;

public class ResultSaleDTO implements Serializable{

	private static final long serialVersionUID = 2815301459427989809L;
	private Integer custommersNumber;
	private Integer sellersNumber;
	private Integer mostExpensiveSaleId;
	private String worstSellerName;
	
	public Integer getCustommersNumber() {
		return custommersNumber;
	}
	public void setCustommersNumber(Integer custommersNumber) {
		this.custommersNumber = custommersNumber;
	}
	public Integer getSellersNumber() {
		return sellersNumber;
	}
	public void setSellersNumber(Integer sellersNumber) {
		this.sellersNumber = sellersNumber;
	}
	public Integer getMostExpensiveSaleId() {
		return mostExpensiveSaleId;
	}
	public void setMostExpensiveSaleId(Integer mostExpensiveSaleId) {
		this.mostExpensiveSaleId = mostExpensiveSaleId;
	}
	public String getWorstSellerName() {
		return worstSellerName;
	}
	public void setWorstSellerName(String worstSellerName) {
		this.worstSellerName = worstSellerName;
	}
	@Override
	public String toString() {
		return "custommersNumber=" + custommersNumber + ", sellersNumber=" + sellersNumber
				+ ", mostExpensiveSaleId=" + mostExpensiveSaleId + ", worstSellerName=" + worstSellerName;
	}
}
