package com.becoder.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.entity.FileDetails;
import com.becoder.entity.Notes;

public interface NotesService {
		
	public List<NotesDto> getAllNotes();

    public Boolean saveNotes(String notes, MultipartFile file) throws Exception;

	public byte[] downloadFile(FileDetails fileDetails) throws Exception;

	public FileDetails getFileDetails(Integer id) throws Exception;

	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize);

	NotesResponse getAllNotesByUser(Integer userId);
	
}
