package com.maybank.assesment.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maybank.assesment.product.dto.Product;
import com.maybank.assesment.product.entity.ProductEntity;
import com.maybank.assesment.product.exception.ProductException;
import com.maybank.assesment.product.service.ProductService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;


@RequestMapping("/api/vi/product")
@RestController
public class ProductController {
	private static final Logger logger = LogManager.getLogger(ProductController.class);
	
	private ProductService productService;
	
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@PostMapping("/save")
	public ResponseEntity<Object> createProduct(@RequestBody Product product){
		logger.info("calling save endpoint with request :{}",product); //4. log the request 
		try {
			var res = productService.saveProduct(product);		
			Map<String, Object> response = new HashMap<>();
			response.put("status", "success");
			response.put("httpStatus", HttpStatus.OK);
			response.put("payload", res);
			response.put("message_en", "");
			response.put("message_ms", "");
			logger.info("response of save endpoint :{}",response); //4. log the response 
			return new ResponseEntity<>(response,HttpStatus.OK);
		}catch(ProductException ex) {
			Map<String, Object> response = new HashMap<>();
			response.put("status", ex.getErrorCode());
			response.put("httpStatus", ex.getHttpStatus());
			response.put("payload", null);
			response.put("message_en", ex.getMessage_en());
			response.put("message_ms", ex.getMessage_ms());
			logger.info("response of save endpoint :{}",response);
			return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping("/getProducts")
	public ResponseEntity<Object> getProducts(@RequestParam(value = "page", defaultValue = "0") int page,@RequestParam(value = "size", defaultValue = "10") int size){
		//6. GET method with pagination
		try {
			Page<ProductEntity> p =productService.getAllProducts(page, size);
			if(p.isEmpty()) {
				Map<String, Object> response = new HashMap<>();
				response.put("status", "success");
				response.put("httpStatus", HttpStatus.NOT_FOUND);
				response.put("payload", null);
				response.put("page", page);
				response.put("size", size);
				response.put("totalPage", 0);
				response.put("totalCount", 0);
				response.put("message_en", "Unable to fetch the products");
				response.put("message_ms", "Senarai produk tidak berjaya diperolehi");
				return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
			}else {				
				Map<String, Object> response = new HashMap<>();
				response.put("status", "success");
				response.put("httpStatus", HttpStatus.OK);
				response.put("payload", populateProduct(p));
				response.put("page", page);
				response.put("size", size);
				response.put("totalPage", p.getTotalPages());
				response.put("totalCount", p.getTotalElements());
				response.put("message_en", "Successfully fetch the products");
				response.put("message_ms", "Senanrai produk berjaya diperolehi");
				return new ResponseEntity<>(response,HttpStatus.OK);
			}
		}catch(ProductException ex) {
			Map<String, Object> response = new HashMap<>();
			response.put("status", ex.getErrorCode());
			response.put("httpStatus", ex.getHttpStatus());
			response.put("payload", null);
			response.put("message_en", ex.getMessage_en());
			response.put("message_ms", ex.getMessage_ms());
			return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	private List<Product> populateProduct(Page<ProductEntity> productEntityList){
		
		var result = new ArrayList<Product>();
		productEntityList.stream().forEach( c -> {
				var product = new Product(); 
				product.setId(c.getId());
				product.setProductName(c.getProductName());
				product.setProductCode(c.getProductCode());
				product.setProductDesc(c.getProductDesc());
				product.setMadeIn(c.getMadeIn());
				product.setManufacturer(c.getManufacturer());
				product.setManufacturerAddress(c.getManufacturerAddress());
				product.setDistributor(c.getDistributor());
				product.setDistributorAddress(c.getDistributorAddr());
				product.setBestBefore(c.getBestBefore());
				result.add(product);
			}
		);
		return result;		
	}
	
}
