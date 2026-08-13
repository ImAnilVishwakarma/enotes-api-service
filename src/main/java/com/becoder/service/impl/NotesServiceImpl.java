package com.becoder.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.runtime.ObjectMethods;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.entity.FileDetails;
import com.becoder.entity.Notes;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.CategoryRepo;
import com.becoder.repository.FileRepository;
import com.becoder.repository.NotesRespository;
import com.becoder.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotesServiceImpl implements NotesService{

	@Autowired  
	private NotesRespository notesRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CategoryRepo repo;
	
	@Value("${file.upload.path}")
	private String uploadPath;
	
	@Autowired
	private FileRepository fileRepo;
	
	@Override
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception {
		
	    ObjectMapper objectMapper = new ObjectMapper();
	    NotesDto notesDto = objectMapper.readValue(notes, NotesDto.class);

	    notesDto.setIsDeleted(false);
	    notesDto.setDeletedOn(null);
	    
	    // Category validation
	    CheckCategoryExist(notesDto.getCategory());

	    // DTO -> Entity
	    Notes notesMap = mapper.map(notesDto, Notes.class);

	    // Save file details
		FileDetails fileDtls = saveFileDetails(file);

		if (!ObjectUtils.isEmpty(fileDtls)) {
			notesMap.setFileDetails(fileDtls);
		} else {
			notesMap.setFileDetails(null);
		}
		Notes saveNotes = notesRepo.save(notesMap);
		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
	}

	  

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {

		   if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
				   String originalFilename = file.getOriginalFilename();
				   String extension = FilenameUtils.getExtension(originalFilename);
				   List<String> extensionAllow = Arrays.asList("pdf", "xlsx", "jpg", "png", "docx");
				   if (!extensionAllow.contains(extension)) {
				   throw new IllegalArgumentException("invalid file format ! Upload only .pdf , .xlsx , .jpg ");
				   }
		   
		
			FileDetails fileDtls = new FileDetails();

			fileDtls.setOriginalFileName(originalFilename);
			fileDtls.setDisplayFileName(getDisplayName(originalFilename));

			String rndString = UUID.randomUUID().toString();
			String uploadfileName = rndString + "." + extension;

			fileDtls.setUploadFileName(uploadfileName);
			fileDtls.setFileSize(file.getSize());
			File saveFile = new File(uploadPath);
			
			if (!saveFile.exists()) {
				saveFile.mkdir();
			}
			String storePath = uploadPath.concat(uploadfileName);
			fileDtls.setPath(storePath);
			
			// upload file 
			long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
			if(upload!=0)
			{
			  FileDetails saveFileDtls = fileRepo.save(fileDtls);
			  return saveFileDtls; 
			}
		  }
		return null;
	}
	private String getDisplayName(String originalFilename) {	
		// java_programming_tutorials.pdf
		// java_prog.pdf
		String extension = FilenameUtils.getExtension(originalFilename);
		String fileName = FilenameUtils.removeExtension(originalFilename);
		if (fileName.length() > 8) {
			fileName = fileName.substring(0, 7);
		}
		fileName = fileName + " " + extension;
		return fileName;
	}

	private void CheckCategoryExist(CategoryDto category) throws Exception{
 
		repo.findById(category.getId()).orElseThrow(()-> new ResourceNotFoundException("Category id invalid"));

	}

	@Override
	public List<NotesDto> getAllNotes() {
		return notesRepo.findAll().stream()
				.map(notes -> mapper.map(notes, NotesDto.class)).toList();	
	}

	@Override
	public byte[] downloadFile(FileDetails fileDetails) throws Exception {
		
	  InputStream io = new FileInputStream(fileDetails.getPath());
	  return StreamUtils.copyToByteArray(io);	
	}

	@Override
	public FileDetails getFileDetails(Integer id) throws Exception {
		FileDetails fileDtls = fileRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("File is not available"));
		  return fileDtls;
	}



	@Override
	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo ,Integer pageSize) {
		Pageable pageable = (Pageable) PageRequest.of(pageNo, pageSize);  // page indexing starting from
		Page<Notes> pageNotes = (Page<Notes>) notesRepo.findByCreatedByAndIsDeletedFalse(userId, pageable);
		List<NotesDto> noteDto = pageNotes.getContent().stream().map(n -> mapper.map(n, NotesDto.class)).toList();	  
	    NotesResponse notes = NotesResponse.builder()
	    		.notes(noteDto)
	    		.pageNo(pageNotes.getNumber () )
	    		.pageSize(pageNotes.getSize ())
	    		.totalElements(pageNotes.getTotalElements())
	    		.totalPages(pageNotes.getTotalPages ())
	    		.isFirst(pageNotes.isFirst())
	    		.isLast(pageNotes.isLast ())
	    		.build();
		return notes;
	}
	

	@Override
	public NotesResponse getAllNotesByUser(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void softDeleteNotes(Integer id) throws Exception {
		Notes notes = notesRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes id invalid ! Not found"));
		notes.setIsDeleted(true);
		notes.setDeletedOn(new Date());
		notesRepo.save(notes);
	}

	@Override
	public void restoreNotes(Integer id) throws Exception {
		Notes notes = notesRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes id invalid ! Not found"));
		notes.setIsDeleted(false);
		notes.setDeletedOn(null);
		notesRepo.save(notes);
	}

	@Override
	public List<NotesDto> getUserRecycleBinNotes(Integer userld) {
	 	List<Notes> recycleNotes = notesRepo.findByCreatedByAndIsDeletedTrue(userld);
		List<NotesDto>notesDtoList = recycleNotes.stream().map(note -> mapper.map(note, NotesDto.class)).toList();
		return notesDtoList;
	}		
}
