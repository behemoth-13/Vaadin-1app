package com.example.myapp1.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.example.myapp1.dao.JPAUtility;
import com.example.myapp1.dao.entity.Hotel;

public class HotelService {

	private static HotelService instance;
	private static final Logger LOGGER = Logger.getLogger(HotelService.class.getName());

	private EntityManager em = JPAUtility.getEntityManager();

	private HotelService() {
	}

	public static HotelService getInstance() {
		if (instance == null) {
			instance = new HotelService();
		}
		return instance;
	}

	public synchronized List<Hotel> findAll() {
		TypedQuery<Hotel> namedQuery = em.createNamedQuery("Hotel.findAll", Hotel.class);
        return namedQuery.getResultList();
	}

	public synchronized List<Hotel> findAll(String filter) {
		List<Hotel> hotels;
		
		if (filter == null || filter.isEmpty()) {
			TypedQuery<Hotel> namedQuery = em.createNamedQuery("Hotel.findAll", Hotel.class);
			hotels = namedQuery.getResultList();
		} else {
			hotels = em.createNamedQuery("Hotel.filter", Hotel.class).
		            setParameter("filter", "%" + filter.toLowerCase() + "%").
		            getResultList();
		}
		return hotels;
	}
	
	public synchronized List<Hotel> findAll(String nameFilter, String addressFilter) {
		List<Hotel> hotels;
		
		if ((nameFilter == null || nameFilter.isEmpty())
				&& (addressFilter == null || addressFilter.isEmpty())) {
			TypedQuery<Hotel> namedQuery = em.createNamedQuery("Hotel.findAll", Hotel.class);
			hotels = namedQuery.getResultList();
		} else if (addressFilter == null || addressFilter.isEmpty()) {
			hotels = em.createNamedQuery("Hotel.filterByName", Hotel.class).
            setParameter("filter", "%" + nameFilter.toLowerCase() + "%").
            getResultList();
		} else if (nameFilter == null || nameFilter.isEmpty()) {
			hotels = em.createNamedQuery("Hotel.filterByAddress", Hotel.class).
		            setParameter("filter", "%" + addressFilter.toLowerCase() + "%").
		            getResultList();
		} else {
			hotels = em.createNamedQuery("Hotel.filterByNameAndAddress", Hotel.class).
		            setParameter("filterByName", "%" + nameFilter.toLowerCase() + "%").
		            setParameter("filterByAddress", "%" + addressFilter.toLowerCase() + "%").
		            getResultList();
		}

		return hotels;
	}

	public synchronized long count() {
		return ((Number) em.createNamedQuery("Category.count")
				.getSingleResult()).longValue();
	}

	public synchronized void delete(Hotel value) {
		em.getTransaction().begin();
        value = em.find(Hotel.class, value.getId());
        em.remove(value);
        em.getTransaction().commit();
	}

	public synchronized void save(Hotel entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Hotel is null.");
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
	
	public synchronized void refreshHotels() {
		List<Hotel> hotelsList = findAll();
		for(Hotel hotel:hotelsList){
			em.refresh(hotel);
		}
	}
	public synchronized boolean isExistHotel(Hotel checkHotel) {
		return (em.createNamedQuery("Hotel.equals")
				.setParameter("name", checkHotel.getName())
				.setParameter("address", checkHotel.getAddress())
				.setParameter("category", checkHotel.getCategory())
				.setParameter("rating", checkHotel.getRating())
				.setParameter("operatesFrom", checkHotel.getOperatesFrom())
				.setParameter("url", checkHotel.getUrl())
				.setParameter("description", checkHotel.getDescription())
				.getResultList()).size() > 0;
	}

}
