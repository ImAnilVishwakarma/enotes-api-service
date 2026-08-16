package com.becoder.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.TodoDto;
import com.becoder.dto.TodoDto.StatusDto;
import com.becoder.enums.TodoStatus;
import com.becoder.exception.ResourceNotFoundException;

@Component
public class Validation {

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
}