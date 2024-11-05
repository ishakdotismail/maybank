package com.maybank.assesment.product.service;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maybank.assesment.product.dto.Product;
import com.maybank.assesment.product.entity.ProductEntity;
import com.maybank.assesment.product.repository.ProductRepository;
import com.maybank.assesment.product.util.ProductUtil;
import com.maybank.assesment.product.exception.ProductException;


@Service
@Transactional  //5 use transactional for data integrity
public class ProductService {
	
	private static final Logger logger = LogManager.getLogger(ProductService.class);
	
	private ProductRepository productRepo;
	
	public ProductService(ProductRepository productRepository) {
		this.productRepo = productRepository;
	}
	
	public Product saveProduct(Product product) {
		var productEntity = populateProductEntity(product);
		try {
			productRepo.save(productEntity); //5. Save record into DB using Hibernate
			ProductUtil.sendEmailNotification("ishak.ismail@gmail.com"); //7. Calling external API to send out the notification
		}catch(Exception e) {
			throw new ProductException(
					"BAD_REQUEST",
					"Failed to save the product",
					"Maklumat produk tidak berjaya disimpan",
					HttpStatus.BAD_REQUEST);
		}
		return product;
	}
	
	public Page<ProductEntity> getAllProducts(int page, int size){
		try {
			Pageable pageable = PageRequest.of(page, size);
			return productRepo.findAllWithPagination(pageable);
		}catch(Exception e) {
			throw new ProductException(
					"INTERNAL_SERVER_ERROR",
					"Exception while fetching the product",
					"Ralat semasa mendapatkan senarai produk",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private ProductEntity populateProductEntity(Product product) {
		
		var productEntity = new ProductEntity();
		productEntity.setId(ProductUtil.generateProductId());
		productEntity.setProductName(product.getProductName());
		productEntity.setProductDesc(product.getProductDesc());
		productEntity.setBestBefore(product.getBestBefore());
		productEntity.setMadeIn(product.getMadeIn());
		productEntity.setManufacturer(product.getManufacturer());
		productEntity.setManufacturerAddress(product.getManufacturerAddress());
		productEntity.setDistributor(product.getDistributor());
		productEntity.setDistributorAddr(product.getManufacturerAddress());
		productEntity.setCreatedDatetime(new Date());
		return productEntity;
	}
}
