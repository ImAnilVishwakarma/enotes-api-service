package com.becoder.service;


import java.net.http.HttpRequest;

import org.springframework.stereotype.Service;

import com.becoder.dto.LoginRequest;
import com.becoder.dto.LoginResponse;
import com.becoder.dto.PasswordChngRequest;
import com.becoder.dto.PswdResetRequest;
import com.becoder.dto.UserRequest;
import com.becoder.entity.User;
import com.becoder.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public interface UserService {

	public void changePassword (PasswordChngRequest passwordRequest);
	
	public Boolean register(UserRequest userDto, String url) throws Exception ;
	
	public void setRole(UserRequest userDto, User user);

	public LoginResponse login(LoginRequest loginRequest);

	public void sendEmailPasswordRest(String email, HttpServletRequest request) throws ResourceNotFoundException, Exception;

	public void verifyPswdRestLink(Integer uid, String code) throws Exception;

	public void resetPassword(PswdResetRequest pswdResetRequest) throws Exception;
	
}
