package com.jyothirlatha.customerservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jyothirlatha.customerservice.model.CustomerEntity;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, String> {

}
