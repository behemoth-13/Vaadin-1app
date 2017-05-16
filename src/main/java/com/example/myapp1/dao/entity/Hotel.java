package com.example.myapp1.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@NamedQueries({
	   @NamedQuery(name = "Hotel.findAll", query = "SELECT h from Hotel h"),
	   @NamedQuery(name = "Hotel.findByCategory", query = "SELECT h from Hotel h WHERE h.category = :categoryFilter"),
	   @NamedQuery(name = "Hotel.count", query = "SELECT COUNT(h) FROM Hotel h"),
	   @NamedQuery(name = "Hotel.filter", query = "SELECT h FROM Hotel AS h WHERE (LOWER(h.name) LIKE :filter) "
	   		+ "OR (LOWER(h.address) LIKE :filter) OR (h.operatesFrom LIKE :filter) "
	   		+ "OR (LOWER(h.description) LIKE :filter) OR (LOWER(h.url) LIKE :filter)"),
	   @NamedQuery(name = "Hotel.filterByName", query = "SELECT h FROM Hotel AS h WHERE LOWER(h.name) LIKE :filter"),
	   @NamedQuery(name = "Hotel.filterByAddress", query = "SELECT h FROM Hotel AS h WHERE LOWER(h.address) LIKE :filter"),
	   @NamedQuery(name = "Hotel.filterByNameAndAddress", query = "SELECT h FROM Hotel AS h WHERE "
	   		+ "(LOWER(h.name) LIKE :filterByName) AND (LOWER(h.address) LIKE :filterByAddress)"),
	   @NamedQuery(name = "Hotel.equals", query = "SELECT h FROM Hotel AS h WHERE "
		   		+ "(h.name = :name) AND (h.address = :address) AND (h.category = :category) AND (h.rating = :rating) AND "
		   		+ "(h.operatesFrom = :operatesFrom) AND (h.url = :url) AND (h.description = :description)")
	})
public class Hotel extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Every hotel must have a name")
    @Size(min = 1, message = "Name should not be empty")
	private String name;

	@NotNull(message = "Every hotel must have an address")
    @Size(min = 1, message = "Address should not be empty")
	private String address;

	@NotNull(message = "Every hotel must have a rating")
	@Min(value = 1, message = "Rating should be a positive number")
	@Max(value = 5, message = "Rating should be a number less or equals 5")
	private Integer rating;

	@NotNull(message = "Every hotel must have a date since it works")
	@Max(value = 10950, message = "Date isn't more senior than 30 years")
	@Column(name = "OPERATES_FROM")
	private Long operatesFrom;

	@ManyToOne
	@JoinColumn(name = "CATEGORY_ID", foreignKey = @ForeignKey(name = "FK_CATEGORY_ID"))
	private Category category;
	
	@Column(columnDefinition = "TEXT")
	private String description;

	@NotNull(message = "Every hotel must have an url")
    @Pattern(regexp = "^(http://|https://)www\\.booking\\.com/.+\\.html$", message = "Url example: https://www.booking.com/.....html")
	private String url;

	public boolean isPersisted() {
		return id != null;
	}

	@Override
	public String toString() {
		return name + " " + rating +"stars " + address;
	}

	@Override
	protected Hotel clone() throws CloneNotSupportedException {
		return (Hotel) super.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((operatesFrom == null) ? 0 : operatesFrom.hashCode());
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		Hotel other = (Hotel) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (operatesFrom == null) {
			if (other.operatesFrom != null)
				return false;
		} else if (!operatesFrom.equals(other.operatesFrom))
			return false;
		if (rating == null) {
			if (other.rating != null)
				return false;
		} else if (!rating.equals(other.rating))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	public Hotel() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Long getOperatesFrom() {
		return operatesFrom;
	}

	public void setOperatesFrom(Long operatesFrom) {
		this.operatesFrom = operatesFrom;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Hotel(Long id, String name, String address, Integer rating, Long operatesFrom, Category category, String url) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.rating = rating;
		this.operatesFrom = operatesFrom;
		this.category = category;
		this.url = url;
	}
}