package com.becoder.dto;

import java.time.LocalDateTime;

import com.becoder.entity.Category;
import com.becoder.entity.FileDetails;
import com.becoder.entity.Notes;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FavouriteNoteDto {
	
	private Integer id;
	private Notes note;
	private Integer userld;


}
