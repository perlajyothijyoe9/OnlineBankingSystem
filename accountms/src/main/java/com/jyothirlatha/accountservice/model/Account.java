package com.jyothirlatha.accountservice.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ACCOUNT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {

	/**
	 * Account Entity used in Repository
	 */
	@Id
	@NotNull(message = "Enter Account number")
	private long accountId;

	@NotBlank(message = "Enter customerId")
	private String customerId;

	@NotNull(message = "Enter currentBalance")
	private double currentBalance;

	@NotBlank(message = "Enter accountType")
	private String accountType;

	@Column(length = 20)
	@NotBlank(message = "Enter ownerName")
	private String ownerName;

	@Transient
	private List<Transaction> transactions;

}