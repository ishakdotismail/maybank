package com.maybank.assesment.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.maybank.assesment.product.dto.Product;
import com.maybank.assesment.product.entity.ProductEntity;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long>{

	@Query("from ProductEntity ORDER BY createdDatetime DESC")
	public Page<ProductEntity> findAllWithPagination(Pageable page);
}
