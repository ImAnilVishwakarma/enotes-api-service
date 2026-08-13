package com.becoder.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotesDto {
    private Integer id;
	private String title;
	
	private String description;

	private CategoryDto category; 
	
    private Integer createdBy;
	
	private Date createdOn;
	
	private Integer updatedBy;
	
	private Date updatedOn;
	
	private FileDto fileDetails;
	
	private Boolean isDeleted;
   
	private Date deletedOn;
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	public static class FileDto{
		private Integer id;
		private String uploadFileName; 
		private String originalFileName;
		private String displayFileName;
		private String path;
		private Long fileSize;
	}

}
