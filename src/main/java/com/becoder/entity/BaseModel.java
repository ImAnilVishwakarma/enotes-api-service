package com.becoder.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@MappedSuperclass
public abstract class BaseModel {
	@CreatedBy
	private Integer createdBy;
	@CreatedDate
	private Date createdOn;
	@LastModifiedBy
	private Integer updatedBy;
	@LastModifiedDate
	private Date updatedOn;
	
}