package com.becoder.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.TodoDto;
import com.becoder.dto.TodoDto.StatusDto;
import com.becoder.dto.UserDto;
import com.becoder.enums.TodoStatus;
import com.becoder.exception.ExistDataException;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.RoleRepository;
import com.becoder.repository.UserRepository;

@Component
public class Validation {

	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserRepository userRepo;

	public void categoryValidation(CategoryDto categoryDto) {

		Map<String, Object> error = new LinkedHashMap<>();

		if (ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("Category Object/JSON shouldn't be null or empty");
		} else {

			// validation name field

			if (ObjectUtils.isEmpty(categoryDto.getName())) {
				error.put("name", "Name field is empty or null");
			} else {
				if (categoryDto.getName().length() < 3) {
					error.put("name", "Name length min 3");
				}
				if (categoryDto.getName().length() > 100) {
					error.put("name", "Name length max 100");
				}
			}
			// Description Validation
			if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
				error.put("description", "Description field is empty or null");
			}

			// isActive Validation
			if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
				error.put("isActive", "isActive field is empty or null");
			} else {
				if (categoryDto.getIsActive() != Boolean.TRUE.booleanValue()
						&& categoryDto.getIsActive() != Boolean.FALSE.booleanValue())
					;
			}
		}

		if (!error.isEmpty()) {
			throw new IllegalArgumentException(error.toString());
		}
	}

	public void todoValidation(TodoDto todo) throws Exception {
		StatusDto regStatus = todo.getStatus();

		Boolean statusFound = false;
		for (TodoStatus st : TodoStatus.values()) {
			if (st.getId().equals(regStatus.getId())) {
				statusFound = true;
			}
		}
		if (!statusFound) {
			throw new ResourceNotFoundException("invalid status");
		}
	}

	public void userValidation(UserDto userDto) {

	    if (!StringUtils.hasText(userDto.getFirstName())) {
	        throw new IllegalArgumentException("first name is invalid");
	    }

	    if (!StringUtils.hasText(userDto.getLastName())) {
	        throw new IllegalArgumentException("last name is invalid");
	    }

	    if (!StringUtils.hasText(userDto.getEmail())
	            || !userDto.getEmail().matches(Constants.Email_Regix)) {
	        throw new IllegalArgumentException("email is invalid");
	    }else {
	    	
	    	// validate email exist
			Boolean existEmail = userRepo.existsByEmail(userDto.getEmail());
			if (existEmail) {
				throw new ExistDataException("Email already exist");
			}

		}

	    if (!StringUtils.hasText(userDto.getMobNo())
	            || !userDto.getMobNo().matches(Constants.MOBNO_REGEX)) {
	        throw new IllegalArgumentException("mob is invalid");
	    }

	    if (CollectionUtils.isEmpty(userDto.getRoles())) {
	        throw new IllegalArgumentException("role is invalid");
	    } else {

	        List<Integer> roleIds = roleRepo.findAll()
	                .stream()
	                .map(r -> r.getId())
	                .toList();

	        List<Integer> invalidRegRoleIds = userDto.getRoles()
	                .stream()
	                .map(r -> r.getId())
	                .filter(roleId -> !roleIds.contains(roleId))
	                .toList();

	        if (!CollectionUtils.isEmpty(invalidRegRoleIds)) {
	            throw new IllegalArgumentException(
	                    "role is invalid " + invalidRegRoleIds
	            );
	        }
	    }
	}	
	
	
}