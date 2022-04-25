package com.fitness.app.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;
import com.fitness.app.entity.AdminPay;

@Repository
@EnableMongoRepositories
public interface AdminPayRepo extends MongoRepository<AdminPay, String> {

	
	public AdminPay findByVendorAndStatus(String vendor, String status);
	
	public List<AdminPay> findByVendor(String vendor);
}
