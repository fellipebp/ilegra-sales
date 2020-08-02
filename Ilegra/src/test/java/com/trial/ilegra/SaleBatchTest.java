package com.trial.ilegra;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.trial.ilegra.component.salebatch.interpreter.SaleBatchInterpreter;
import com.trial.ilegra.component.salebatch.mapper.SaleBatchMapper;
import com.trial.ilegra.model.dto.ResultSaleDTO;

@RunWith(SpringRunner.class)
public class SaleBatchTest {

	private SaleBatchInterpreter saleBatchInterpreter;
	
	@BeforeEach	
	public void setup() {
		saleBatchInterpreter = new SaleBatchInterpreter(new SaleBatchMapper());
	}
	
	@Test
	public void shouldProcessAllData() {
		List<String> lines = asList("001ç92138765059çPedroç50000", 
					  "001ç76514770061çPauloç40000.99",
					  "002ç75759551000132çJose da SilvaçRural", 
					  "002ç32104057000127çEduardo PereiraçRural", 
					  "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro", 
					  "003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo");
		ResultSaleDTO result = saleBatchInterpreter.process(lines);
		assertThat(result.getCustommersNumber()).isEqualTo(2);
		assertThat(result.getMostExpensiveSaleId()).isEqualTo(10);
		assertThat(result.getSellersNumber()).isEqualTo(2);
		assertThat(result.getWorstSellerName()).isEqualTo("Paulo");
	}
	
	@Test
	public void shouldProcessOnlyOneSeller() {
		List<String> lines = asList("xxxxxxxxxxxx", 
					  "001ç76514770061çPauloç40000.99",
					  "002ç75759551000132çJose da SilvaçRural", 
					  "002ç32104057000127çEduardo PereiraçRural", 
					  "xxxxxxxxxxxxxxxxxxxxxxxxxx", 
					  "003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo");
		ResultSaleDTO result = saleBatchInterpreter.process(lines);
		assertThat(result.getCustommersNumber()).isEqualTo(2);
		assertThat(result.getMostExpensiveSaleId()).isEqualTo(8);
		assertThat(result.getSellersNumber()).isEqualTo(1);
		assertThat(result.getWorstSellerName()).isEqualTo("Paulo");
	}
	
	
	
}
