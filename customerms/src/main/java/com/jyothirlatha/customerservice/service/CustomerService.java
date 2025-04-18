package com.jyothirlatha.customerservice.service;

import com.jyothirlatha.customerservice.model.AuthenticationResponse;
import com.jyothirlatha.customerservice.model.CustomerEntity;

public interface CustomerService {

	public CustomerEntity createCustomer(String token, CustomerEntity customer);

	public CustomerEntity getCustomerDetail(String tokrn, String id);

	public AuthenticationResponse hasEmployeePermission(String token);

	public boolean deleteCustomer(String id);

	AuthenticationResponse hasCustomerPermission(String token);

	public AuthenticationResponse hasPermission(String token);

	public CustomerEntity saveCustomer(String token, CustomerEntity customer);

	public CustomerEntity updateCustomer(String token, CustomerEntity customer);

}
