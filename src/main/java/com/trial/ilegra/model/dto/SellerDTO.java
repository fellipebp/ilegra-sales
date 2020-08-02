package com.trial.ilegra.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SellerDTO implements Serializable {

	private static final long serialVersionUID = 4798599142050364973L;
	
	private String name;
	private BigDecimal sellValue;
	
	public SellerDTO() {
	}
	
	public SellerDTO(String name, BigDecimal sellValue) {
		this.name = name;
		this.sellValue = sellValue;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getSellValue() {
		return sellValue;
	}
	public void setSellValue(BigDecimal sellValue) {
		this.sellValue = sellValue;
	}
	
}
