package com.becoder.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.Category;


public interface CategoryRepo extends JpaRepository<Category, Integer>{
	Optional<Category> findByIdAndIsDeletedFalse(Integer id);
	List<Category> findByIsDeletedFalse();
	List<Category> findByIsActiveTrueAndIsDeletedFalse();
	Boolean existsByName(String trim);
}
