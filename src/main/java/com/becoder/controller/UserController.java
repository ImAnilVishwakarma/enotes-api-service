package com.becoder.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.PasswordChngRequest;
import com.becoder.dto.UserRequest;
import com.becoder.dto.UserResponse;
import com.becoder.entity.User;
import com.becoder.service.UserService;
import com.becoder.util.CommonUtil;

@RestController
@RequestMapping ("/api/v1/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper mapper;
	
	@PostMapping("/")
	public ResponseEntity<?> registerUser(@RequestBody UserRequest userDto) throws Exception {
		Boolean register = userService.register(userDto, null);
		if (register) {
			return CommonUtil.createBuildResponseMessage("Register success", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponse("Register Failed", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile() {
		User loggedInUser = CommonUtil.getLoggedInUser();
		UserResponse userResponse = mapper.map(loggedInUser, UserResponse.class);
		return CommonUtil.createBuildResponse(userResponse, HttpStatus.OK);
	}
	
	@PostMapping("/chag-pswd")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChngRequest passwordRequest) {
        userService.changePassword(passwordRequest); 
		return CommonUtil.createBuildResponseMessage("Password Changes Success", HttpStatus.OK);
	}
}
 