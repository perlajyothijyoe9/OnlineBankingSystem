package com.jyothirlatha.transactionservice.service;

import com.jyothirlatha.transactionservice.util.AccountInput;
import com.jyothirlatha.transactionservice.util.TransactionInput;

public interface TransactionServiceInterface {

	public boolean makeTransfer(String token, TransactionInput transactionInput);

	public boolean makeWithdraw(String token, AccountInput accountInput);

	public boolean makeDeposit(String token, AccountInput accountInput);

	public boolean makeServiceCharges(String token, AccountInput accountInput);
}
