package com.maybank.assesment.product.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Product {
	
	private long id;
	private String productName;
	private String productCode;
	private String productDesc;
	private String madeIn;
	private Date bestBefore;
	private String manufacturer;
	private String manufacturerAddress;
	private String distributor;
	private String distributorAddress;

}
