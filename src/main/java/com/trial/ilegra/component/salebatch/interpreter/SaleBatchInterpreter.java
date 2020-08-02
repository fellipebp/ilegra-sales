package com.trial.ilegra.component.salebatch.interpreter;

import static com.trial.ilegra.component.salebatch.SaleBatchConstants.COLUMN_SEPARATOR;
import static com.trial.ilegra.component.salebatch.SaleBatchConstants.CUSTOMER_ID;
import static com.trial.ilegra.component.salebatch.SaleBatchConstants.MOST_EXPENSIVE_SALE;
import static com.trial.ilegra.component.salebatch.SaleBatchConstants.SELLER_ID;
import static com.trial.ilegra.component.salebatch.SaleBatchConstants.WORST_SELLER;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trial.ilegra.component.salebatch.SaleBatchAnalyzer;
import com.trial.ilegra.component.salebatch.SaleBatchCache;
import com.trial.ilegra.component.salebatch.mapper.SaleBatchMapper;
import com.trial.ilegra.exception.SaleBatchAnalyzerException;
import com.trial.ilegra.model.dto.ItemDTO;
import com.trial.ilegra.model.dto.ResultSaleDTO;
import com.trial.ilegra.model.dto.SaleDTO;
import com.trial.ilegra.model.dto.SellerDTO;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;

@Component
public class SaleBatchInterpreter {

	private static final String REGEX_NUMBERS = "^[0-9]+([\\,\\.][0-9]+)?$";
	private static final String REGEX_LETTERS = "^[a-zA-Z\\s]*$";
	private static final Logger LOGGER = Logger.getLogger(SaleBatchAnalyzer.class.getName());
	
	private SaleBatchMapper mapper;
	private SaleBatchCache cache;  
	
	@Autowired
	public SaleBatchInterpreter(SaleBatchMapper saleBatchMapper) {
		this.mapper = saleBatchMapper;
		this.cache = SaleBatchCache.getInstance();
	}

	public ResultSaleDTO process(List<String> lines) {
		for (String line : lines) {
			try {
				List<String> data = asList(line.split(COLUMN_SEPARATOR));
				Integer id = getId(data);
				if (SELLER_ID.equals(id)) {
					processSeller(data);
				} else if (CUSTOMER_ID.equals(id)) {
					processCustomer(data);
				} else {
					processSales(data);
				}
			} catch (SaleBatchAnalyzerException e) {
				LOGGER.log(WARNING, e.getMessage());
			}
		}
		ResultSaleDTO result = mapper.from(cache.getMap());
		cache.clear();
		return result;
	}

	private void processSeller(List<String> line) {
		String cpf = getCPF(line);
		String name = getName(line);
		BigDecimal salary = getSalary(line);
		cache.putSellerNumbers();
		LOGGER.log(FINE, "Seller data: {0}, {1}, {2}", new Object[]{ cpf, name, salary });
	}

	private void processCustomer(List<String> line) {
		String cnpj = getCNPJ(line);
		String name = getName(line);
		String bussinessArea = getBussinessArea(line);
		cache.putCustomerNumbers();
		LOGGER.log(FINE, "Customer data: {0}, {1}, {2}", new Object[]{ cnpj, name, bussinessArea });
	}

	private void processSales(List<String> line) {
		Integer saleId = getSaleId(line);
		List<ItemDTO> itens = getItens(line);
		String salesmanName = getSalesmanName(line);
		mostExpensiveSale(itens, saleId);
		worstSeller(itens, salesmanName);
		LOGGER.log(FINE, "Sales data: {0}, {1}, {2}", new Object[]{ saleId, itens, salesmanName});
	}

	private String getSalesmanName(List<String> sellerLine) {
		String name = sellerLine.get(3);
		boolean onlyLetters = name.matches(REGEX_LETTERS);
		if (!onlyLetters) {
			throw new SaleBatchAnalyzerException("Invalid salesman name: "+ name);
		}
		return name;
	}

	private List<ItemDTO> getItens(List<String> saleLine) {
		String itens = saleLine.get(2);
		List<String> itensParsed = asList(itens.replace("[", "").replace("]", "").split(","));
		List<ItemDTO> itensDTO = new ArrayList<ItemDTO>();
		ItemDTO itemDTO;
		for (String item : itensParsed) {
			String[] itemData = item.split("-");
			Integer itemId = getItemId(itemData[0]);
			Integer itemQuantity = getItemQuantity(itemData[1]);
			BigDecimal itemPrice = getItemPrice(itemData[2]);
			itemDTO = new ItemDTO(itemId, itemQuantity, itemPrice);
			itensDTO.add(itemDTO);
		}
		return itensDTO;
	}

	public Integer getItemId(String item) {
		Optional<Integer> result = ofNullable(item).filter(SaleBatchInterpreter::isDigit).map(Integer::parseInt);

		if (!result.isPresent()) {
			throw new SaleBatchAnalyzerException("Invalid item id: "+ item);
		}
		return result.get();
	}

	public Integer getItemQuantity(String item) {
		Optional<Integer> result = ofNullable(item).filter(SaleBatchInterpreter::isDigit).map(Integer::parseInt);
		if (!result.isPresent()) {
			throw new SaleBatchAnalyzerException("Invalid item quantity: "+ item);
		}
		return result.get();
	}

	public BigDecimal getItemPrice(String item) {
		Optional<BigDecimal> result = ofNullable(item)
				 .filter(SaleBatchInterpreter::isDigit)
				.map(BigDecimal::new);
		if (!result.isPresent()) {
			throw new SaleBatchAnalyzerException("Invalid item price: "+ item);
		}
		return result.get();
	}

	private Integer getSaleId(List<String> saleLine) {
		Optional<Integer> result = ofNullable(saleLine.get(1)).filter(SaleBatchInterpreter::isDigit)
				.map(Integer::parseInt);
		if (!result.isPresent()) {
			throw new SaleBatchAnalyzerException("Invalid sale id: "+ saleLine.get(1));
		}
		return result.get();
	}

	public String getBussinessArea(List<String> custommerLine) {
		return custommerLine.get(3);
	}

	private String getCNPJ(List<String> custommerLine) {
		CNPJValidator cnpjValidator = new CNPJValidator();
		String cnpj = custommerLine.get(1);
		cnpjValidator.assertValid(cnpj);
		List<ValidationMessage> errors = cnpjValidator.invalidMessagesFor(cnpj);
		if (errors.size() > 0) {
			throw new RuntimeException("");
		}
		return cnpj;
	}

	private BigDecimal getSalary(List<String> sellerLine) {
		Optional<BigDecimal> result = ofNullable(sellerLine.get(3))
									.filter(SaleBatchInterpreter::isDigit)
				.map(BigDecimal::new);
		if (!result.isPresent()) {
			throw new SaleBatchAnalyzerException("Invalid salary: "+ sellerLine.get(3));
		}
		return result.get();
	}

	private String getName(List<String> sellerLine) {
		String name = sellerLine.get(2);
		boolean onlyLetters = name.matches(REGEX_LETTERS);
		if (!onlyLetters) {
			throw new SaleBatchAnalyzerException("Invalid salesman name: "+ name);
		}
		return name;
	}

	private String getCPF(List<String> sellerLine) {
		CPFValidator cpfValidator = new CPFValidator();
		String cpf = sellerLine.get(1);
		List<ValidationMessage> errors = cpfValidator.invalidMessagesFor(cpf);
		if (errors.size() > 0) {
			throw new SaleBatchAnalyzerException("Invalid CPF: "+ cpf);
		}
		return cpf;
	}

	private Integer getId(List<String> sellerLine) {
		Optional<Integer> result = ofNullable(sellerLine.get(0)).filter(SaleBatchInterpreter::isDigit)
				.map(Integer::parseInt);
		if (!result.isPresent()) {
			throw new SaleBatchAnalyzerException("Invalid id: "+ sellerLine.get(0));
		}
		return result.get();
	}

	private void mostExpensiveSale(List<ItemDTO> itens, Integer saleId) {
		BigDecimal sumSellPrice = sumSellPrice(itens);
		SaleDTO saleDTO = new SaleDTO(saleId, sumSellPrice);
		if (!cache.containsKey(MOST_EXPENSIVE_SALE)) {
			cache.putSale(saleDTO);
			return;
		} 
		SaleDTO sale = (SaleDTO) cache.get(MOST_EXPENSIVE_SALE);
		if (sale.getPrice().compareTo(saleDTO.getPrice()) < 0) {
			cache.putSale(saleDTO);
		}
	}

	public void worstSeller(List<ItemDTO> itens, String salesmanName) {
		BigDecimal sumSellPrice = sumSellPrice(itens);
		SellerDTO sellerDTO = new SellerDTO(salesmanName, sumSellPrice);
		if (!cache.containsKey(WORST_SELLER)) {
			cache.putSeller(sellerDTO);
			return;
		} 
		SellerDTO seller = (SellerDTO) cache.get(WORST_SELLER);
		if (seller.getSellValue().compareTo(sellerDTO.getSellValue()) > 0 ) {
			cache.putSeller(sellerDTO);
		}
	}
	
	private BigDecimal sumSellPrice(List<ItemDTO> itens) {
		return  itens.stream()
				  .map(a -> a.getPrice().multiply(BigDecimal.valueOf(a.getQuantity())))
				  .reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public static boolean isDigit(String input) {
		return input.matches(REGEX_NUMBERS);
	}

}
