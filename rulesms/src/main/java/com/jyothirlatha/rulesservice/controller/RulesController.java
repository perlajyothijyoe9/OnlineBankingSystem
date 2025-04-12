package com.jyothirlatha.rulesservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.jyothirlatha.rulesservice.exception.MinimumBalanceException;
import com.jyothirlatha.rulesservice.feign.AccountFeign;
import com.jyothirlatha.rulesservice.model.Account;
import com.jyothirlatha.rulesservice.model.AccountInput;
import com.jyothirlatha.rulesservice.model.RulesInput;
import com.jyothirlatha.rulesservice.service.RulesService;

@RestController
public class RulesController {

	private static final String INVALID = "Send Valid Details.";
	@Autowired
	public RulesService rulesService;
	@Autowired
	AccountFeign accountFeign;

	@PostMapping("/evaluateMinBal")
	public ResponseEntity<?> evaluate(@RequestBody RulesInput account)
			throws MinimumBalanceException {
		// Jwt token is checked
		// check the accountId is Not null
		if (account.getCurrentBalance() == 0) {
			throw new MinimumBalanceException(INVALID);
		} else {
			boolean status = rulesService.evaluate(account);

			return new ResponseEntity<Boolean>(status, HttpStatus.OK);
		}
	}

	// Service charges calculation
	@PostMapping("/serviceCharges")
	public ResponseEntity<?> serviceCharges(@RequestHeader("Authorization") String token) {
		// Jwt token is checked
		rulesService.hasPermission(token);

		// accountFeign.servicecharge(token, new AccountInput(account.getAccountId(),
		// detected));
		try {
			List<Account> body = accountFeign.getAllacc(token).getBody();
			for (Account acc : body) {
				double detected = acc.getCurrentBalance() / 10;
				if (acc.getCurrentBalance() < 1000 && (acc.getCurrentBalance() - detected) > 0)
					accountFeign.servicecharge(token, new AccountInput(acc.getAccountId(), detected));

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return ResponseEntity.ok(accountFeign.getAllacc(token).getBody());
	}

	// If Feign Microservice is not working this fallback method is executed
	public ResponseEntity<String> evalMinimumBalanceFallback() {// @RequestHeader("Authorization") String
																// token@RequestBody RulesInput account)
		return new ResponseEntity<String>("Minimum balance criteria fail", HttpStatus.BAD_GATEWAY);
	}

}
