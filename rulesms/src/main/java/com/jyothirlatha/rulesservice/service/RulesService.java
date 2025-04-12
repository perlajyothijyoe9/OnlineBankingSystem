package com.jyothirlatha.rulesservice.service;

import com.jyothirlatha.rulesservice.model.AuthenticationResponse;
import com.jyothirlatha.rulesservice.model.RulesInput;
import com.jyothirlatha.rulesservice.model.ServiceResponse;

public interface RulesService {

	public boolean evaluate(RulesInput account);

	public AuthenticationResponse hasPermission(String token);

	public ServiceResponse serviceCharges(RulesInput account);

}
