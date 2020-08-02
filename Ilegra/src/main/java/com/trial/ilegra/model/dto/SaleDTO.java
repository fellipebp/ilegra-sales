package com.trial.ilegra.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SaleDTO implements Serializable{

	private static final long serialVersionUID = 5530207664643982423L;
	
	private Integer id;
	private BigDecimal price;
	
	public SaleDTO() {
	}
	
	public SaleDTO(Integer id, BigDecimal price) {
		this.id = id;
		this.price = price;
	}

	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
