package com.jyothirlatha.customerservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jyothirlatha.customerservice.exception.AccessDeniedException;
import com.jyothirlatha.customerservice.exception.ConsumerAlreadyExistException;
import com.jyothirlatha.customerservice.exception.ServiceFailException;
import com.jyothirlatha.customerservice.feign.AccountFeign;
import com.jyothirlatha.customerservice.feign.AuthorizationFeign;
import com.jyothirlatha.customerservice.model.Account;
import com.jyothirlatha.customerservice.model.AppUser;
import com.jyothirlatha.customerservice.model.AuthenticationResponse;
import com.jyothirlatha.customerservice.model.CustomerEntity;
import com.jyothirlatha.customerservice.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

	private static final String CUSTOMER = "CUSTOMER";
	@Autowired
	AuthorizationFeign authorizationFeign;

	@Autowired
	AccountFeign accountFeign;

	@Autowired
	CustomerRepository customerRepo;

	@Override
	public AuthenticationResponse hasPermission(String token) {
		return authorizationFeign.getValidity(token);
	}

	@Override
	public AuthenticationResponse hasEmployeePermission(String token) {
		AuthenticationResponse validity = authorizationFeign.getValidity(token);
		if (!authorizationFeign.getRole(validity.getUserid()).equals("EMPLOYEE"))
			throw new AccessDeniedException("NOT ALLOWED");
		else
			return validity;
	}

	@Override
	public AuthenticationResponse hasCustomerPermission(String token) {
		AuthenticationResponse validity = authorizationFeign.getValidity(token);
		if (!authorizationFeign.getRole(validity.getUserid()).equals(CUSTOMER))
			throw new AccessDeniedException("NOT ALLOWED");
		else
			return validity;
	}

	@Override
	// @HystrixCommand(fallbackMethod="welcomeFallBack", commandProperties=
	// {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",
	// value="120000")})
	public CustomerEntity createCustomer(String token, CustomerEntity customer) {

		CustomerEntity checkCustomerExists = getCustomerDetail(token, customer.getUserid());
		if (checkCustomerExists != null) {
			throw new ConsumerAlreadyExistException("Customer already exist");
		} else {
			AppUser user = new AppUser(customer.getUserid(), customer.getUsername(), customer.getPassword(), null,
					CUSTOMER);
			authorizationFeign.createUser(user);
		}

		for (Account acc : customer.getAccounts()) {
			accountFeign.createAccount(token, customer.getUserid(), acc);
		}

		customerRepo.save(customer);
		log.info("Consumer details saved.");
		return customer;
	}

	@Override
	// @HystrixCommand(fallbackMethod="welcomeFallBack", commandProperties=
	// {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",
	// value="120000")})
	public CustomerEntity getCustomerDetail(String token, String id) {
		Optional<CustomerEntity> customer = customerRepo.findById(id);
		if (!customer.isPresent())
			return null;
		log.info("Consumer details fetched.");
		List<Account> list = accountFeign.getCustomerAccount(token, id);
		customer.get().setAccounts(list);
		return customer.get();
	}

	@Override
	public boolean deleteCustomer(String id) {
		CustomerEntity customer = customerRepo.findById(id).get();
		if (customer != null)
			customerRepo.deleteById(id);
		else
			return false;
		log.info("Consumer details deleted.");
		return true;
	}

	@Override
	public CustomerEntity saveCustomer(String token, CustomerEntity customer) {
		CustomerEntity checkCustomerExists = getCustomerDetail(token, customer.getUserid());
		if (checkCustomerExists == null) {
			AppUser user = new AppUser(customer.getUserid(), customer.getUsername(), customer.getPassword(), null,
					CUSTOMER);
			authorizationFeign.createUser(user);
		}
		return customerRepo.save(customer);
	}

	@Override
	public CustomerEntity updateCustomer(String token, CustomerEntity customer) {
		CustomerEntity toUpdate = customerRepo.findById(customer.getUserid()).get();
		toUpdate.setAddress(customer.getAddress());
		toUpdate.setDateOfBirth(customer.getDateOfBirth());
		toUpdate.setPan(customer.getPan());
		toUpdate.setPassword(customer.getPassword());
		toUpdate.setUsername(customer.getUsername());
		for (Account acc : customer.getAccounts()) {
			accountFeign.updateAccount(token, acc);
		}
		toUpdate.setAccounts(customer.getAccounts());
		return customerRepo.save(toUpdate);
	}

	public CustomerEntity welcomeFallBack(String token, String id) throws ServiceFailException {
		throw new ServiceFailException("server down");
	}

}
