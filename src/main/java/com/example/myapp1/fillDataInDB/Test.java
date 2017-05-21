package com.example.myapp1.fillDataInDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.myapp1.dao.entity.Category;
import com.example.myapp1.dao.entity.Hotel;
import com.example.myapp1.dao.entity.Payment;
import com.example.myapp1.service.CategoryService;
import com.example.myapp1.service.HotelService;

public class Test {
	/*private static CategoryService categoryService = CategoryService.getInstance();
	private static HotelService hotelService = HotelService.getInstance();*/
	private static final String[] CATEGORY_DATA = new String[] {
				"Hotel", "Hostel", "GuestHouse", "Appartments" };
	private static final String[] HOTEL_DATA = new String[] {
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
	private static Random r = new Random(0);
	
	/*public static void main(String[] args) {
		fillCategories();
		fillHotels();
		System.exit(0);
	}
	
	private static void fillCategories() {
		for (String category : CATEGORY_DATA) {
			Category c = new Category();
			c.setCategory(category);
			categoryService.save(c);
		}
	}
	
	private static void fillHotels() {
		List<Hotel> list = getHotels();
		for (Hotel h : list) {
			h.setCategory(categoryService.findAll().get(r.nextInt((int)categoryService.count())));
			hotelService.save(h);
		}
	}*/
	
	public static String[] getCategories() {
		return CATEGORY_DATA;
	}
	
	public static List<Hotel> getHotels() {
		List<Hotel> list = new ArrayList<>();
		for (String hotel : HOTEL_DATA) {
			String[] split = hotel.split(";");
			Hotel h = new Hotel();
			h.setName(split[0]);
			h.setRating(Integer.parseInt(split[1]));
			h.setUrl(split[2]);
			h.setAddress(split[3]);
			//h.setCategory(
			//h.setCategory(categoryService.findAll().get(r.nextInt((int)categoryService.count())));
			long daysOld = r.nextInt(365 * 30);
			h.setOperatesFrom(daysOld);
			h.setDescription(split[4]);
			switch (r.nextInt(2)) {
				case 0: h.setPayment(null);
				break;
				case 1: h.setPayment(new Payment(r.nextInt(101)));
				break;
			}
			list.add(h);
		}
		return list;
	}
}
