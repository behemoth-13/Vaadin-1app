package com.example.myapp1.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryService {
	
	private static CategoryService instance;
	private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());
	
	private final HashMap<Long, Category> categories = new HashMap<>();
	private long nextId = 0;
	
	private CategoryService() {
	}

	public static CategoryService getInstance() {
		if (instance == null) {
			instance = new CategoryService();
			instance.ensureTestData();
		}
		return instance;
	}
	
	public synchronized List<Category> findAll() {
		ArrayList<Category> arrayList = new ArrayList<>();
		for (Category category : categories.values()) {
			try {
				arrayList.add(category.clone());
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(CategoryService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Category>() {

			@Override
			public int compare(Category o1, Category o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}
	
	public synchronized long count() {
		return categories.size();
	}
	
	public synchronized Category getDefault() {
		Category def = null;
		if (categories.values().iterator().hasNext()) {
			def = categories.values().iterator().next();
		}
		return def;
	}

	public synchronized void delete(Category value) {
		categories.remove(value.getId());
	}
	
	public synchronized void save(Category entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Category is null.");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (Category) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		categories.put(entry.getId(), entry);
	}
	
	public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] categoryData = new String[] {
					"Hotel", "Hostel", "GuestHouse", "Appartments" };
			for (String category : categoryData) {
				Category c = new Category();
				c.setCategory(category);
				save(c);
			}
		}
	}
}
