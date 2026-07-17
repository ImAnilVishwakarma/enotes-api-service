package com.becoder.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.Date;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryResponse;
import com.becoder.entity.Category;
import com.becoder.exception.ExistDataException;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.CategoryRepo;
import com.becoder.service.CategoryService;
import com.becoder.util.Validation;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo repo;
	
	@Autowired
	private ModelMapper mapper;
	
	
	
	@Autowired
	private Validation validation;
	
	private Optional<Category> byId;
	private CrudRepository<Category, Integer> categoryRepo;

	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {
		
//		System.out.println(categoryDto.getDescription());
			
//		Category category = new Category();
//		
//		category.setName(categoryDto.getName());
//		category.setDescription(categoryDto.getDescription());
//		category.setIsActive(categoryDto.getIsActive());
		
// ModelMapper Annotation used 
				
		validation.categoryValidation(categoryDto);
		
	    Boolean exist = repo.existsByName(categoryDto.getName().trim());
	    if(exist) {
	    	
	    	throw new ExistDataException("Category already exist");
	    	
	    }
		
		Category category = mapper.map(categoryDto, Category.class);	
	
		if(ObjectUtils.isEmpty(category.getId())) 
		{
			category.setIsDeleted(false);
	//		category.setCreatedBy(1) ;
			category.setCreatedOn(new Date () ) ;	 
		}else {
			updateCategory(category);
		}
			
		Category saveCategory = repo.save(category);
		if (ObjectUtils.isEmpty(saveCategory)) {
			return false;
		} else {
			return true;
		}
	}
	

	public void updateCategory(Category category) {
		Optional<Category> findById = repo.findById(category.getId());
		if(findById.isPresent()) {
			Category existCategory = findById.get();
			category.setCreatedBy(existCategory.getCreatedBy());
			category.setCreatedOn(existCategory.getCreatedOn());
			category.setIsDeleted(existCategory.getIsDeleted());
			
	//		category.setUpdatedBy(1);
	//		category.setUpdatedOn(new Date());
			
		}
	}


	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = repo.findByIsDeletedFalse();
		List<CategoryDto> categoryDtoList = categories.stream().map(cat-> mapper.map(cat, CategoryDto.class)).toList();
		return categoryDtoList;
	}


	@Override
	public Boolean saveCategory(Category category) {
		return saveCategory(category);
	}


	@Override
	public List<CategoryResponse> getActiveCategory() {
		List<Category> categories = repo.findByIsActiveTrueAndIsDeletedFalse();
		List<CategoryResponse> CategoryList = categories.stream().map(cat-> mapper.map(cat, CategoryResponse.class)).toList();
		return CategoryList;
	}


	@Override
	public CategoryDto getCategoryById(Integer id) throws Exception {
		  Category category = repo.findByIdAndIsDeletedFalse(id)
				  .orElseThrow(()->new ResourceNotFoundException("Category not found with id="+id));
		  if(!ObjectUtils.isEmpty(category)) {
			  if(category.getName () == null) {
				  throw new IllegalArgumentException("Name Is Null");
			  }

			  return mapper.map(category, CategoryDto.class);
		  }
		  return null; 
	}   


	@Override
	public Boolean deleteCategory(Integer id) {
		Optional<Category> findByCategory = repo.findById(id);
		  if(findByCategory.isPresent()) {
			  Category category = findByCategory.get();
			  category.setIsDeleted(true);
			  repo.save(category);
			  return true;
		  }
		  return null;
	}
}
