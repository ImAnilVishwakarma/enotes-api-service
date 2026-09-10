package com.becoder.service.impl;

import org.springframework.stereotype.Service;

import com.becoder.dto.LoginRequest;
import com.becoder.dto.LoginResponse;
import com.becoder.dto.UserRequest;
import com.becoder.service.AuthService;


@Service
public class AuthServiceImpl implements AuthService{

	@Override
	public Boolean register(UserRequest userDto, String url) throws Exception {

		return null;
	}

	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		return null;
	}

}
