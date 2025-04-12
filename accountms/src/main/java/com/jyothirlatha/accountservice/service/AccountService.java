package com.jyothirlatha.accountservice.service;

import java.util.List;

import com.jyothirlatha.accountservice.model.Account;
import com.jyothirlatha.accountservice.model.AccountCreationStatus;
import com.jyothirlatha.accountservice.model.AccountInput;
import com.jyothirlatha.accountservice.model.AuthenticationResponse;

public interface AccountService {

	/**
	 * Interface used for Service offered in Account service
	 */

	public AccountCreationStatus createAccount(String customerId, Account account);

	public List<Account> getCustomerAccount(String token, String customerId);

	public Account getAccount(long accountId);

	public AuthenticationResponse hasPermission(String token);

	public AuthenticationResponse hasEmployeePermission(String token);

	public AuthenticationResponse hasCustomerPermission(String token);

	public Account updateDepositBalance(AccountInput accountInput);

	public Account updateBalance(AccountInput accountInput);

	public Account updateAccount(Account account);

	public List<Account> getAllAccounts();

}
