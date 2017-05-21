package com.example.myapp1.fillDataInDB;

import static com.example.myapp1.ElementId.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import com.example.myapp1.UI.form.converter.DateConverter;
import com.example.myapp1.dao.entity.Hotel;
import com.example.myapp1.dao.entity.Payment;

/*
 * 1. drop all rows in hotel table and category tables
 * 2. set up constants in this class
 */

public class InsertWithSelenium {
	private static WebDriver driver;
	private static final String BASE_URL = "http://localhost:8080";
	private static final String DRIVER_PATH = "F:\\java\\vaadin\\chromedriver.exe";
	private static final String DRIVER = "webdriver.chrome.driver";
	private static final int SLEEP_TIME_LONG = 8000;// For open browser and initialization
	private static final int SLEEP_TIME_TINY = 1500; // After click any button
	
	public static void main(String... args) throws InterruptedException {
		System.setProperty(DRIVER, DRIVER_PATH);
		driver = new ChromeDriver();
		driver.get(BASE_URL);
		Thread.sleep(SLEEP_TIME_LONG);
		
		fillCategories();
		fillHotels();
		
		Thread.sleep(SLEEP_TIME_TINY);
		driver.quit();
	}
	
	static void selectCategoryPage() throws InterruptedException {
		WebElement categoryPage = driver.findElement(By.xpath("//*[@id='mainLayout']/div/div[3]/div/span[3]"));
		categoryPage.click();
		Thread.sleep(SLEEP_TIME_TINY);
	}
	
	static void selectHotelPage() throws InterruptedException {
		WebElement hotelPage = driver.findElement(By.xpath("//*[@id='mainLayout']/div/div[3]/div/span[1]"));
		hotelPage.click();
		Thread.sleep(SLEEP_TIME_TINY);
	}
	
	static void fillField(String fieldId, String value) {
		WebElement field = driver.findElement(By.id(fieldId));
		field.clear();
		field.sendKeys(value);
	}
	
	static void fillDateField(String dateFieldId, String value) {
		WebElement dateField = driver.findElement(By.id(dateFieldId)).findElement(By.className("v-datefield-textfield"));
		dateField.clear();
		dateField.sendKeys(value);
	}
	
	static void fillNativeSelect(String nativeSelectId, String visibleText) {
		WebElement nativeSelect = driver.findElement(By.id(nativeSelectId)).findElement(By.className("v-select-select"));
		Select select = new Select(nativeSelect);
		select.selectByVisibleText(visibleText);
	}
	
	static void pushRadioButton(String radioButtonId, String value) {
		List<WebElement> buttons = driver.findElement(By.id(radioButtonId)).findElements(By.className("v-select-option"));
		for (int i = 0; i < buttons.size(); i++) {
			WebElement w = buttons.get(i).findElement(By.tagName("LABEL"));
			if (w.getText().equals(value)) {
				buttons.get(i).click();
			}
		}
	}

	static void pushButton(String buttonId) throws InterruptedException {
		WebElement newCategoryButton = driver.findElement(By.id(buttonId));
		newCategoryButton.click();
		Thread.sleep(SLEEP_TIME_TINY);
	}
	
	static void fillCategories() throws InterruptedException {
		selectCategoryPage();
		String[] categories = Test.getCategories();
		for (String category : categories) {
			pushButton(CATEGORYVIEW_ADD_CATEGORY_BTN);
			fillField(CATEGORYFORM_CATEGORYFIELD, category);
			pushButton(CATEGORYFORM_SAVE_BTN);
		}
	}
	
	static void fillHotels() throws InterruptedException {
		selectHotelPage();
		String[] categories = Test.getCategories();
		Random r = new Random(0);
		List<Hotel> hotels = Test.getHotels();
		for (Hotel hotel : hotels) {
			pushButton(HOTELVIEW_ADDHOTEL_BTN);
			fillField(HOTELFORM_NAME_FIELD, hotel.getName());
			fillField(HOTELFORM_ADDRESS_FIELD, hotel.getAddress());
			fillField(HOTELFORM_RATING_FIELD, hotel.getRating().toString());
			fillDateField(HOTELFORM_OPERATESFROM_DATE_FIELD, getDateStringFromOperatesFrom(hotel));
			fillNativeSelect(HOTELFORM_CATEGORY_NATIVE_SELECT, categories[r.nextInt(categories.length)]);
			fillField(HOTELFORM_DESCRIPTION_FIELD, hotel.getDescription());
			fillField(HOTELFORM_URL_FIELD, hotel.getUrl());
			fillPayment(hotel.getPayment());

			pushButton(HOTELFORM_SAVE_BTN);
		}
	}
	
	static String getDateStringFromOperatesFrom(Hotel hotel) {
		DateConverter converter = new DateConverter();
		LocalDate date = converter.convertToPresentation(hotel.getOperatesFrom(), null);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		return  date.format(formatter);
	}
	
	static void fillPayment(Payment payment) {
		if (payment.getDeposit() == null) {
			pushRadioButton(PAYMENTFIELD_RADIO_BUTTONS, "Cash");
		} else {
			pushRadioButton(PAYMENTFIELD_RADIO_BUTTONS, "CreditCard");
			fillField(PAYMENTFIELD_DEPOSIT_FIELD, payment.getDeposit().toString());
		}
	}
}