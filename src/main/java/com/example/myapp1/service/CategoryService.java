package com.example.myapp1.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.example.myapp1.dao.JPAUtility;
import com.example.myapp1.dao.entity.Category;

public class CategoryService {
	
	private static CategoryService instance;
	private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());
	
	private EntityManager em = JPAUtility.getEntityManager();
	
	private CategoryService() {
	}

	public static CategoryService getInstance() {
		if (instance == null) {
			instance = new CategoryService();
		}
		return instance;
	}
	
	public synchronized List<Category> findAll() {
		//ArrayList<Category> arrayList = new ArrayList<>();
		
		TypedQuery<Category> namedQuery = em.createNamedQuery("Category.findAll", Category.class);
        return namedQuery.getResultList();
		
		/*Collections.sort(arrayList, new Comparator<Category>() {

			@Override
			public int compare(Category o1, Category o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;*/
	}
	
	public synchronized long count() {
		return ((Number) em.createNamedQuery("Category.count")
				.getSingleResult()).longValue();
	}
	
	public synchronized Category getDefault() {
		return em.createNamedQuery("Category.findAll", Category.class).setMaxResults(1).getSingleResult();
	}

	public synchronized void delete(Category value) {
		em.getTransaction().begin();
		em.remove(em.contains(value) ? value : em.merge(value));
        em.getTransaction().commit();
	}
	
	public synchronized void save(Category entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Category is null.");
			return;
		}
		if (entry.getId() == null) {
			em.getTransaction().begin();
			em.persist(entry);
			em.getTransaction().commit();
		} else {
			em.getTransaction().begin();
            em.merge(entry);
            em.getTransaction().commit();
        }
	}
	
	/*public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] categoryData = new String[] {
					"Hotel", "Hostel", "GuestHouse", "Appartments" };
			for (String category : categoryData) {
				Category c = new Category();
				c.setCategory(category);
				save(c);
			}
		}
	}*/

	public synchronized boolean isExistCategory(Category checkCategory) {
		return (em.createNamedQuery("Category.containsName")
				.setParameter("name", checkCategory.getCategory())
				.getResultList()).size() > 0;
	}
	
	public synchronized void refreshCategories() {
		List<Category> categoriesList = findAll();
		for(Category category : categoriesList){
			em.refresh(category);
		}
	}
}