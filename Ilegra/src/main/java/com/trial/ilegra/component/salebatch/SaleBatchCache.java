package com.trial.ilegra.component.salebatch;

import static com.trial.ilegra.component.salebatch.SaleBatchConstants.MOST_EXPENSIVE_SALE;
import static com.trial.ilegra.component.salebatch.SaleBatchConstants.SELLER_NUMBERS;
import static com.trial.ilegra.component.salebatch.SaleBatchConstants.WORST_SELLER;
import static com.trial.ilegra.component.salebatch.SaleBatchConstants.CUSTOMER_NUMBERS;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.trial.ilegra.model.dto.SaleDTO;
import com.trial.ilegra.model.dto.SellerDTO;

public class SaleBatchCache {
	
	private static final SaleBatchCache dataCache = new SaleBatchCache();
	private Map<String, Object> map;

	public SaleBatchCache() {
		map = new ConcurrentHashMap<>();
	}
	public static SaleBatchCache getInstance() {
		return dataCache;
	}
	
    public Map<String, Object> getMap() {
        return map;
    }
	
    public void put(String key, Object data) {
		map.put(key, data);
    }
    
    public void putSeller(SellerDTO sellerDTO) {
		map.put(WORST_SELLER, sellerDTO);
	}
	
    public void putSale(SaleDTO saleDTO) {
		map.put(MOST_EXPENSIVE_SALE, saleDTO);
	}
    
    public void putSellerNumbers() {
    	 map.put(SELLER_NUMBERS, (Integer) getOrDefault(SELLER_NUMBERS) + 1);
	}
    
    public void putCustomerNumbers() {
    	map.put(CUSTOMER_NUMBERS, (Integer) getOrDefault(CUSTOMER_NUMBERS) + 1);
	}
    
    public boolean containsKey(String key) {
  		return map.containsKey(key);
     }
    
    public Object get(String key) {
  		return map.get(key);
    }
    
    public Object getOrDefault(String key) {
  		return map.getOrDefault(key, 0);
    }
    
    public void clear() {
  		map.clear();
    }
	
}
