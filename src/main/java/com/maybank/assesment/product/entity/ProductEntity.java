package com.maybank.assesment.product.entity;


import java.util.Date;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="T_PRODUCT")
@Data
public class ProductEntity {
	
	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="NAME")
	private String productName;
	
	@Column(name="CODE")
	private String productCode;
	
	@Column(name="DESCRIPTION")
	private String productDesc;
	
	@Column(name="MADEIN")
	private String madeIn;
	
	@Column(name="BEST_BEFORE")
	private Date bestBefore;
	
	@Column(name="MANUFACTURER")
	private String manufacturer;
	
	@Column(name="MANUFACTURER_ADDR")
	private String manufacturerAddress;
	
	@Column(name="DISTRIBUTOR")
	private String distributor;
	
	@Column(name="DISTRIBUTOR_ADDR")
	private String distributorAddr;
	
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;
	
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;
}
