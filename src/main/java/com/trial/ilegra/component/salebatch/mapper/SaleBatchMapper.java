package com.trial.ilegra.component.salebatch.mapper;

import static com.trial.ilegra.component.salebatch.SaleBatchConstants.CUSTOMER_NUMBERS;
import static com.trial.ilegra.component.salebatch.SaleBatchConstants.MOST_EXPENSIVE_SALE;
import static com.trial.ilegra.component.salebatch.SaleBatchConstants.SELLER_NUMBERS;
import static com.trial.ilegra.component.salebatch.SaleBatchConstants.WORST_SELLER;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trial.ilegra.model.dto.ResultSaleDTO;
import com.trial.ilegra.model.dto.SaleDTO;
import com.trial.ilegra.model.dto.SellerDTO;

@Component
public class SaleBatchMapper {

	@Autowired
	public SaleBatchMapper() {
	}
	
	public ResultSaleDTO from(Map<String, Object> map) {
		SellerDTO seller = (SellerDTO) map.get(WORST_SELLER);
		SaleDTO sale = (SaleDTO) map.get(MOST_EXPENSIVE_SALE);
		Integer custommerNumbers = (Integer) map.get(CUSTOMER_NUMBERS);
		Integer sellerNumbers = (Integer) map.get(SELLER_NUMBERS);
		ResultSaleDTO resultSaleDTO = new ResultSaleDTO();
		resultSaleDTO.setCustommersNumber(custommerNumbers);
		resultSaleDTO.setMostExpensiveSaleId(sale.getId());
		resultSaleDTO.setSellersNumber(sellerNumbers);
		resultSaleDTO.setWorstSellerName(seller.getName());
		return resultSaleDTO;
	}
}
