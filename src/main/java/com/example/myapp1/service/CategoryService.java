package com.example.myapp1.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import com.example.myapp1.dao.JPAUtility;
import com.example.myapp1.dao.entity.Category;

//@Transactional
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
		TypedQuery<Category> namedQuery = em.createNamedQuery("Category.findAll", Category.class);
        return namedQuery.getResultList();
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
		em.getTransaction().begin();
		if (entry.getId() == null) {
			em.persist(entry);
		} else {
            em.merge(entry);
        }
		em.getTransaction().commit();
	}

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