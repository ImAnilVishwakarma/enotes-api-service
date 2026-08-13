package com.becoder.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.Notes;

public interface NotesRespository extends JpaRepository<Notes, Integer>{

	Page findByCreatedBy(Integer userId, Pageable pageable);
	List<Notes> findByCreatedByAndIsDeletedTrue(Integer userld);
	Page<Notes> findByCreatedByAndIsDeletedFalse(Integer userId, Pageable pageable);
	List<Notes> findAllByIsDeletedAndDeletedOnBefore(boolean b, LocalDateTime cutOffDate);

	
}
