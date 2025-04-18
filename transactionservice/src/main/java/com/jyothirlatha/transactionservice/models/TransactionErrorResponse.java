package com.jyothirlatha.transactionservice.models;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionErrorResponse {
	/**
	 * TransactionErrorResponse for returning Error response
	 */

	private LocalDateTime timestamp;
	private HttpStatus status;
	private String reason;
	private String message;
}
