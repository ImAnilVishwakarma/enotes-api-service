package com.becoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.Notes;

public interface NotesRespository extends JpaRepository<Notes, Integer>{

	
}
