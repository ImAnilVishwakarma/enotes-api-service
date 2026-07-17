package com.becoder.controller;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryResponse;
import com.becoder.entity.Category;
import com.becoder.exception.GlobalExceptionHandler;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.service.CategoryService;
import com.becoder.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final GlobalExceptionHandler globalExceptionHandler;
	
	@Autowired
	private CategoryService categoryService;

    CategoryController(GlobalExceptionHandler globalExceptionHandler) {
        this.globalExceptionHandler = globalExceptionHandler;
    }

	@PostMapping("/save")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) {
		Boolean saveCategory = categoryService.saveCategory(categoryDto);
		if (saveCategory) {
		 return	CommonUtil.createBuildResponseMessage("saved success", HttpStatus.CREATED);
//			return new ResponseEntity<>("saved success", HttpStatus.CREATED);
		} else {
			 return	CommonUtil.createErrorResponseMessage("Category Not saved", HttpStatus.INTERNAL_SERVER_ERROR);
		//	return new ResponseEntity<>("note saved ", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/")
	public ResponseEntity<?> getAllCategory() {
		
		  List<CategoryDto> allCategory = categoryService.getAllCategory();
		 		
	//	Set<CategoryDto> allCategory = (Set<CategoryDto>) categoryService.getAllCategory(); try to learn set
		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
		//	return new ResponseEntity<>(allCategory, HttpStatus.OK);

					
		}
	}
	
	@GetMapping("/active")
	public ResponseEntity<?> getActiveCategory() {
		List<CategoryResponse> allCategory = categoryService.getActiveCategory();
		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			
			return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
			
	//		return new ResponseEntity<>(allCategory, HttpStatus.OK);
			
		} 
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) {
		try {
			CategoryDto categoryDto = categoryService.getCategoryById(id);
			if (ObjectUtils.isEmpty(categoryDto)) {
				return CommonUtil.createErrorResponseMessage("Internal Server Error ", HttpStatus.NOT_FOUND);
			}
			return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
		} 
		catch (ResourceNotFoundException e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);

	}
		catch (Exception e)
	{
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id) {
        Boolean delete = categoryService.deleteCategory(id);
		if(delete) {
			return CommonUtil.createBuildResponse("Category deleted success", HttpStatus.OK);
	//		return new ResponseEntity<>("Category Delete Successfully", HttpStatus.OK);
		}else
		{
			return new ResponseEntity<>("Category Not Deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
