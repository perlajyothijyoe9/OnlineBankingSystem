package com.jyothirlatha.transactionservice.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jyothirlatha.transactionservice.exception.MinimumBalanceException;
import com.jyothirlatha.transactionservice.feign.AccountFeign;
import com.jyothirlatha.transactionservice.feign.RulesFeign;
import com.jyothirlatha.transactionservice.models.Account;
import com.jyothirlatha.transactionservice.models.RulesInput;
import com.jyothirlatha.transactionservice.models.Transaction;
import com.jyothirlatha.transactionservice.repository.TransactionRepository;
import com.jyothirlatha.transactionservice.util.AccountInput;
import com.jyothirlatha.transactionservice.util.TransactionInput;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionService implements TransactionServiceInterface {

	@Autowired
	private AccountFeign accountFeign;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private RulesFeign ruleFeign;

	/*
	 * Service layer method for making a transfer
	 */
	@Override
	public boolean makeTransfer(String token, TransactionInput transactionInput) throws MinimumBalanceException {

		Account sourceAccount = null;
		Account targetAccount = null;
		if (transactionInput.getAmount() <= 0)
			return false;
		long sourceAccountNumber = transactionInput.getSourceAccount().getAccountId();
		sourceAccount = accountFeign.getAccount(token, sourceAccountNumber);
		Boolean check = (Boolean) ruleFeign.evaluate(new RulesInput(sourceAccount.getAccountId(),
				sourceAccount.getCurrentBalance(), transactionInput.getAmount())).getBody();
		// String name = body.toString();
		if (check.booleanValue() == false)
			throw new MinimumBalanceException("Minimum Balance 1000 should be maintaind");
		long targetAccountNumber = transactionInput.getTargetAccount().getAccountId();
		targetAccount = accountFeign.getAccount(token, targetAccountNumber);

		if (sourceAccount != null && targetAccount != null) {
			if (isAmountAvailable(transactionInput.getAmount(), sourceAccount.getCurrentBalance())) {
				Transaction sourcetransaction = new Transaction();
				sourcetransaction.setAmount(transactionInput.getAmount());
				sourcetransaction.setSourceAccountId(sourceAccount.getAccountId());
				sourcetransaction.setSourceOwnerName(sourceAccount.getOwnerName());
				sourcetransaction.setTargetAccountId(targetAccount.getAccountId());
				sourcetransaction.setTargetOwnerName(targetAccount.getOwnerName());
				sourcetransaction.setInitiationDate(LocalDateTime.now());
				sourcetransaction.setReference("transfer");
				transactionRepository.save(sourcetransaction);
				return true;
			}
		}
		return false;
	}

	/*
	 * To check whether the amount is available
	 */
	private boolean isAmountAvailable(double amount, double accountBalance) {
		log.info("method to check wether the amount is available");
		return (accountBalance - amount) > 0;
	}

	/*
	 * Service layer method for making a withdraw
	 */
	@Override
	public boolean makeWithdraw(String token, AccountInput accountInput) {
		log.info("method to make a withdraw");
		Account sourceAccount = null;

		long accNumber = accountInput.getAccountId();
		sourceAccount = accountFeign.getAccount(token, accNumber);

		Boolean check = (Boolean) ruleFeign.evaluate(new RulesInput(accountInput.getAccountId(),
				sourceAccount.getCurrentBalance(), accountInput.getAmount())).getBody();
		// String bodyToString = body.toString();
		if (check.booleanValue() == false)
			throw new MinimumBalanceException("Minimum Balance 1000 should be maintaind");

		if (sourceAccount != null) {
			Transaction transaction = new Transaction();
			transaction.setSourceAccountId(sourceAccount.getAccountId());
			transaction.setSourceOwnerName(sourceAccount.getOwnerName());
			transaction.setTargetAccountId(sourceAccount.getAccountId());
			transaction.setTargetOwnerName(sourceAccount.getOwnerName());
			transaction.setInitiationDate(LocalDateTime.now());
			transaction.setReference("withdrawn");
			transaction.setAmount(accountInput.getAmount());
			transactionRepository.save(transaction);
			return true;
		}
		return false;
	}

	@Override
	public boolean makeServiceCharges(String token, AccountInput accountInput) {
		log.info("method to make a service charges");
		Account sourceAccount = null;

		long accNumber = accountInput.getAccountId();
		sourceAccount = accountFeign.getAccount(token, accNumber);
		if (sourceAccount != null) {
			Transaction transaction = new Transaction();
			transaction.setSourceAccountId(sourceAccount.getAccountId());
			transaction.setSourceOwnerName(sourceAccount.getOwnerName());
			transaction.setTargetAccountId(sourceAccount.getAccountId());
			transaction.setTargetOwnerName(sourceAccount.getOwnerName());
			transaction.setInitiationDate(LocalDateTime.now());
			transaction.setReference("service charge");
			transaction.setAmount(accountInput.getAmount());
			transactionRepository.save(transaction);
			return true;
		}

		return false;

	}

	/*
	 * Service layer method for making a deposit
	 */
	@Override
	public boolean makeDeposit(String token, AccountInput accountInput) {
		log.info("method to make a deposit");
		Account sourceAccount = null;

		long accNumber = accountInput.getAccountId();
		sourceAccount = accountFeign.getAccount(token, accNumber);
		if (sourceAccount != null) {
			Transaction transaction = new Transaction();
			transaction.setSourceAccountId(sourceAccount.getAccountId());
			transaction.setSourceOwnerName(sourceAccount.getOwnerName());
			transaction.setTargetAccountId(sourceAccount.getAccountId());
			transaction.setTargetOwnerName(sourceAccount.getOwnerName());
			transaction.setInitiationDate(LocalDateTime.now());
			transaction.setReference("deposit");
			transaction.setAmount(accountInput.getAmount());
			transactionRepository.save(transaction);
			return true;
		}
		return false;
	}
}
