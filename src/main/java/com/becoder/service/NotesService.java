package com.becoder.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.FavouriteNoteDto;
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

	public void softDeleteNotes(Integer id) throws Exception;

	public void restoreNotes(Integer id) throws Exception;

	public List<NotesDto> getUserRecycleBinNotes(Integer userld);

	public void hardDeleteNotes(Integer id) throws Exception;

	public void emptyRecycleBin(int userId);
	
	public void favoriteNotes (Integer noteId) throws Exception;
	
	public boolean unFavoriteNotes(Integer noteId) throws Exception;
	
	public List<FavouriteNoteDto>getUserFavoriteNotes () throws Exception;


	
}
