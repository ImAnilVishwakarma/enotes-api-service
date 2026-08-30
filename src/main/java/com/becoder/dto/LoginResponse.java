package com.becoder.dto;

import com.becoder.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
	
	private UserDto user;
	private String token;

}
