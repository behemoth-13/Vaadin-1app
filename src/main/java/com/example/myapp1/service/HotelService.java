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
		
		/*ArrayList<Hotel> arrayList = new ArrayList<>();
		for (Hotel hotel : findAll()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| hotel.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(hotel.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(HotelService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		/*Collections.sort(arrayList, new Comparator<Hotel>() {

			@Override
			public int compare(Hotel o1, Hotel o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;*/
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
	
	/*public synchronized List<Hotel> findByCategory(Category category) {
		List<Hotel> hotels;
		TypedQuery<Hotel> namedQuery = em.createNamedQuery("Hotel.findByCategory", Hotel.class)
				.setParameter("categoryFilter", category);
		hotels = namedQuery.getResultList();
		return hotels;
	}

	public synchronized List<Hotel> findAll(String stringFilter, int start, int maxresults) {
		ArrayList<Hotel> arrayList = new ArrayList<>();
		for (Hotel contact : hotels.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(contact.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(HotelService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Hotel>() {

			@Override
			public int compare(Hotel o1, Hotel o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		int end = start + maxresults;
		if (end > arrayList.size()) {
			end = arrayList.size();
		}
		return arrayList.subList(start, end);
	}*/

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

	/*public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] hotelData = new String[] {
					"3 Nagas Luang Prabang - MGallery by Sofitel;4;https://www.booking.com/hotel/la/3-nagas-luang-prabang-by-accor.en-gb.html;Vat Nong Village, Sakkaline Road, Democratic Republic Lao, 06000 Luang Prabang, Laos;hey",
					"Abby Boutique Guesthouse;1;https://www.booking.com/hotel/la/abby-boutique-guesthouse.en-gb.html;Ban Sawang , 01000 Vang Vieng, Laos;nice hotel",
					"Bountheung Guesthouse;1;https://www.booking.com/hotel/la/bountheung-guesthouse.en-gb.html;Ban Tha Heua, 01000 Vang Vieng, Laos;some description",
					"Chalouvanh Hotel;2;https://www.booking.com/hotel/la/chalouvanh.en-gb.html;13 road, Ban Phonesavanh, Pakse District, 01000 Pakse, Laos;my best choise",
					"Chaluenxay Villa;3;https://www.booking.com/hotel/la/chaluenxay-villa.en-gb.html;Sakkarin Road Ban Xienthong Luang Prabang Laos, 06000 Luang Prabang, Laos; test",
					"Dream Home Hostel 1;1;https://www.booking.com/hotel/la/getaway-backpackers-hostel.en-gb.html;049 Sihome Road, Ban Sihome, 01000 Vientiane, Laos;nice",
					"Inpeng Hotel and Resort;2;https://www.booking.com/hotel/la/inpeng-and-resort.en-gb.html;406 T4 Road, Donekoy Village, Sisattanak District, 01000 Vientiane, Laos;beautiful",
					"Jammee Guesthouse II;2;https://www.booking.com/hotel/la/jammee-guesthouse-vang-vieng1.en-gb.html;Vang Vieng, 01000 Vang Vieng, Laos;bad",
					"Khemngum Guesthouse 3;2;https://www.booking.com/hotel/la/khemngum-guesthouse-3.en-gb.html;Ban Thalat No.10 Road Namngum Laos, 01000 Thalat, Laos;very bad",
					"Khongview Guesthouse;1;https://www.booking.com/hotel/la/khongview-guesthouse.en-gb.html;Ban Klang Khong, Khong District, 01000 Muang Không, Laos;very very bad",
					"Kong Kham Pheng Guesthouse;1;https://www.booking.com/hotel/la/kong-kham-pheng-guesthouse.en-gb.html;Mixay Village, Paksan district, Bolikhamxay province, 01000 Muang Pakxan, Laos;hello",
					"Laos Haven Hotel & Spa;3;https://www.booking.com/hotel/la/laos-haven.en-gb.html;047 Ban Viengkeo, Vang Vieng , 01000 Vang Vieng, Laos;better",
					"Lerdkeo Sunset Guesthouse;1;https://www.booking.com/hotel/la/lerdkeo-sunset-guesthouse.en-gb.html;Muang Ngoi Neua,Ban Ngoy-Nua, 01000 Muang Ngoy, Laos;newer",
					"Luangprabang River Lodge Boutique 1;3;https://www.booking.com/hotel/la/luangprabang-river-lodge.en-gb.html;Mekong River Road, 06000 Luang Prabang, Laos;jetty",
					"Manichan Guesthouse;2;https://www.booking.com/hotel/la/manichan-guesthouse.en-gb.html;Ban Pakham Unit 4/143, 60000 Luang Prabang, Laos;what",
					"Mixok Inn;2;https://www.booking.com/hotel/la/mixok-inn.en-gb.html;188 Sethathirate Road , Mixay Village , Chanthabuly District, 01000 Vientiane, Laos;awesome",
					"Ssen Mekong;2;https://www.booking.com/hotel/la/muang-lao-mekong-river-side-villa.en-gb.html;Riverfront, Mekong River Road, 06000 Luang Prabang, Laos;cool",
					"Nammavong Guesthouse;2;https://www.booking.com/hotel/la/nammavong-guesthouse.en-gb.html;Ban phone houang Sisalearmsak Road , 06000 Luang Prabang, Laos;best",
					"Niny Backpacker hotel;1;https://www.booking.com/hotel/la/niny-backpacker.en-gb.html;Next to Wat Mixay, Norkeokhunmane Road., 01000 Vientiane, Laos;laos",
					"Niraxay Apartment;2;https://www.booking.com/hotel/la/niraxay-apartment.en-gb.html;Samsenthai Road Ban Sihom , 01000 Vientiane, Laos;I see you",
					"Pakse Mekong Hotel;2;https://www.booking.com/hotel/la/pakse-mekong.en-gb.html;No 062 Khemkong Road, Pakse District, Champasak, Laos, 01000 Pakse, Laos;I'll be back",
					"Phakchai Hotel;2;https://www.booking.com/hotel/la/phakchai.en-gb.html;137 Ban Wattay Mueng Sikothabong Vientiane Laos, 01000 Vientiane, Laos;never",
					"Phetmeuangsam Hotel;2;https://www.booking.com/hotel/la/phetmisay.en-gb.html;Ban Phanhxai, Xumnuea, Xam Nua, 01000 Xam Nua, Laos;yes" };

			Random r = new Random(0);
			for (String hotel : hotelData) {
				String[] split = hotel.split(";");
				Hotel h = new Hotel();
				h.setName(split[0]);
				h.setRating(Integer.parseInt(split[1]));
				h.setUrl(split[2]);
				h.setAddress(split[3]);
				h.setCategory(CategoryService.getInstance().findAll()
						.get(r.nextInt((int)CategoryService.getInstance().count())));
				long daysOld = r.nextInt(365 * 30);
				h.setOperatesFrom(daysOld);
				h.setDescription(split[4]);
				save(h);
			}
		}
	}*/
	
	
	//must delete
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
				.getResultList()).size() > 0;
	}

}
