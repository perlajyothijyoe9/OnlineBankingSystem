package com.jyothirlatha.accountservice.exceptionhandling;

public class MinimumBalanceException extends RuntimeException {

	/**
	 * MinimumBalance Exception
	 */
	private static final long serialVersionUID = 1L;

	public MinimumBalanceException() {
		super();
	}

	public MinimumBalanceException(String message) {
		super(message);
	}

}
