package com.becoder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.Date;

import com.becoder.entity.Category;
import com.becoder.repository.CategoryRepo;
import com.becoder.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo repo;

	@Override
	public Boolean saveCategory(Category category) {
		
		category.setIsDeleted(false);
		category.setCreatedBy(1);
		category.setCreatedOn(new Date());
		Category saveCategory = repo.save(category);
		if (ObjectUtils.isEmpty(saveCategory)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<Category> getAllCategory() {
		List<Category> categories = repo.findAll();
		return categories;
	}

}
