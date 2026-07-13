package com.becoder.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@Entity
public class Category extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
	public Category(Integer id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Category(Boolean isActive, Boolean isDeleted, Integer createdBy, Date createdOn, Integer updatedBy,
			Date updatedOn) {
		super(isActive, isDeleted, createdBy, createdOn, updatedBy, updatedOn);
		// TODO Auto-generated constructor stub
	}
	public Category(Boolean isActive, Boolean isDeleted, Integer createdBy, Date createdOn, Integer updatedBy,
			Date updatedOn, Integer id, String name, String description) {
		super(isActive, isDeleted, createdBy, createdOn, updatedBy, updatedOn);
		this.id = id;
		this.name = name;
		this.description = description;
	}
    
    
}