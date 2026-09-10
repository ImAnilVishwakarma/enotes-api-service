package com.becoder.service;


import org.springframework.stereotype.Service;

import com.becoder.dto.LoginRequest;
import com.becoder.dto.LoginResponse;
import com.becoder.dto.PasswordChngRequest;
import com.becoder.dto.UserRequest;
import com.becoder.entity.User;

@Service
public interface UserService {

	public void changePassword (PasswordChngRequest passwordRequest);
	
	public Boolean register(UserRequest userDto, String url) throws Exception ;
	
	public void setRole(UserRequest userDto, User user);

	public LoginResponse login(LoginRequest loginRequest);
	
	

	
	

}
