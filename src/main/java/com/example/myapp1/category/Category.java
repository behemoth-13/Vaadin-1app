package com.example.myapp1.category;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Category implements Serializable, Cloneable {
	
	private Long id;
	
	private String category = "";
	
	public boolean isPersisted() {
		return id != null;
	}
	
	@Override
	public String toString() {
		return category;
	}
	
	@Override
	protected Category clone() throws CloneNotSupportedException {
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

