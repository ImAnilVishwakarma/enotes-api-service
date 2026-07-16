package com.becoder.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@MappedSuperclass
public abstract class BaseModel {
	@CreatedBy
	@Column(updatable = false)
	private Integer createdBy;  
	@CreatedDate
	@Column(updatable = false)
	private Date createdOn;
	@LastModifiedBy
	@Column(insertable = false)
	private Integer updatedBy;
	@LastModifiedDate
	@Column(insertable = false)
	private Date updatedOn;
	
}