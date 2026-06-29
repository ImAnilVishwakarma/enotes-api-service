package com.becoder.service.impl;

import java.util.List;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.Date;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryResponse;
import com.becoder.entity.Category;
import com.becoder.repository.CategoryRepo;
import com.becoder.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo repo;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {
		
//		System.out.println(categoryDto.getDescription());
			
//		Category category = new Category();
//		
//		category.setName(categoryDto.getName());
//		category.setDescription(categoryDto.getDescription());
//		category.setIsActive(categoryDto.getIsActive());
		
// ModelMapper Annotation used 
		
		Category category = mapper.map(categoryDto, Category.class);	
		category.setIsDeleted(false);
		category.setCreatedBy(1) ;
		category.setCreatedOn(new Date () ) ;		
		Category saveCategory = repo.save(category);
		if (ObjectUtils.isEmpty(saveCategory)) {
			return false;
		} else {
			return true;
		}
	}
	

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = repo.findAll();
		List<CategoryDto> categoryDtoList = categories.stream().map(cat-> mapper.map(cat, CategoryDto.class)).toList();
		return categoryDtoList;
	}


	@Override
	public Boolean saveCategory(Category category) {
		// TODO Auto-generated method stub
		return saveCategory(category);
	}


	@Override
	public List<CategoryResponse> getActiveCategory() {
		List<Category> categories = repo.findByIsActiveTrue();
		List<CategoryResponse> CategoryList = categories.stream().map(cat-> mapper.map(cat, CategoryResponse.class)).toList();
		return CategoryList;
	}
}
