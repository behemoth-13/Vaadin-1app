package com.example.myapp1.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@NamedQueries({
   @NamedQuery(name = "Category.findAll", query = "SELECT c from Category c"),
   @NamedQuery(name = "Category.count", query = "SELECT COUNT(c) FROM Category c"),
   @NamedQuery(name = "Category.containsName", query = "SELECT c FROM Category c WHERE c.category = :name")
})
@Table(name = "CATEGORY")
public class Category extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Every hotel must have a category")
	@Column(name = "NAME")
	private String category;
	
	public boolean isPersisted() {
		return id != null;
	}
	
	@Override
	public String toString() {
		return category;
	}
	
	@Override
	public Category clone() throws CloneNotSupportedException {
		return (Category) super.clone();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		return true;
	}
	
	public Category() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Category(Long id, String category) {
		super();
		this.id = id;
		this.category = category;
	}
	
}