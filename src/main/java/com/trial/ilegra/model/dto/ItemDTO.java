package com.trial.ilegra.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemDTO implements Serializable{

	private static final long serialVersionUID = 1939437900081631365L;
	
	private Integer id;
	private Integer quantity;
	private BigDecimal price;
	
	public ItemDTO() {
	}
	
	public ItemDTO(Integer id, Integer quantity, BigDecimal price) {
		this.id = id;
		this.quantity = quantity;
		this.price = price;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
