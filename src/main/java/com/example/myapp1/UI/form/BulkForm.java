package com.example.myapp1.UI.form;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import com.example.myapp1.UI.form.converter.DateConverter;
import com.example.myapp1.UI.views.HotelView;
import com.example.myapp1.dao.entity.Category;
import com.example.myapp1.dao.entity.Hotel;
import com.example.myapp1.service.CategoryService;
import com.example.myapp1.service.HotelService;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class BulkForm extends FormLayout {
	private static final long serialVersionUID = 1L;
	
	HotelView hotelView;
	private NativeSelect<String> chooseItem = new NativeSelect<>("Choose parameter");
	private TextField insertText = new TextField();
	private NativeSelect<String> insertPar = new NativeSelect<>();
	private NativeSelect<Category> insertCat = new NativeSelect<>("Category");
	private DateField insertDate = new DateField("Operates from");
	private Button update = new Button("Update");
	private Button cancel = new Button("Cancel");
	private Binder<Hotel> binder;
	private Set<Hotel> hotels;
	
	private HotelService service = HotelService.getInstance();
	
	public BulkForm(HotelView hotelView) {
		this.hotelView = hotelView;

		setSizeUndefined();
		
		chooseItem.setItems("Name", "Address", "Rating", "Operates from", "Category", "Description", "URL");
		chooseItem.setEmptySelectionAllowed(false);
		chooseItem.setEmptySelectionCaption("Choose parameter");
		
		addComponent(chooseItem);
		
		HorizontalLayout buttons = new HorizontalLayout(update, cancel);
		
		update.setStyleName(ValoTheme.BUTTON_PRIMARY);
		update.setClickShortcut(KeyCode.ENTER);
		
		cancel.addClickListener(e -> setVisible(false));
		
		chooseItem.addValueChangeListener(e -> {
			binder = new Binder<>(Hotel.class);
			insertText.clear();
			removeComponentsIfExist();
			removeComponent(buttons);
			switch (e.getValue()) {
				case "Name" : {
					addComponents(insertText, buttons);
					insertText.setCaption("Insert Name");
					binder.forField(insertText).asRequired("Every hotel must have a name")
					.withValidator(nameVal -> nameVal.trim().length() > 1, "Name should not be greater 1 symbol")
					.bind(Hotel::getName, Hotel::setName);
					update.addClickListener(up -> update("Name"));
					break;
				}
				case "Address" : {
					addComponents(insertText, buttons);
					insertText.setCaption("Insert Address");
					binder.forField(insertText).asRequired("Every hotel must have an address")
					.withValidator(addressVal -> addressVal.trim().length() > 1, "Address should not be empty")
					.bind(Hotel::getAddress, Hotel::setAddress);
					update.addClickListener(up -> update("Address"));
					break;
				}
				case "Rating" : {
					addComponents(insertPar, buttons);
					insertPar.setCaption("Choose Rating");
					insertPar.setItems("1", "2", "3", "4", "5");
					insertPar.setEmptySelectionAllowed(false);
					insertPar.setEmptySelectionCaption("Choose Rating");
					insertPar.setValue("1");
					binder.forField(insertPar).withConverter(new StringToIntegerConverter(1, "Only Digits!"))
					.bind(Hotel::getRating, Hotel::setRating);
					update.addClickListener(up -> update("Rating"));
					break;
				}
				case "Operates from" : {
					addComponents(insertDate, buttons);
					binder.forField(insertDate).withConverter(new DateConverter())
					.asRequired("Every hotel must have a date since it works")
					.withValidator(operatesFromVal -> operatesFromVal < 10950, "Date isn't more senior than 30 years")
					.bind(Hotel::getOperatesFrom, Hotel::setOperatesFrom);
					update.addClickListener(up -> update("Operates from"));
					break;
				}
				case "Category" : {
					addComponents(insertCat, buttons);
					insertCat.setItems(CategoryService.getInstance().findAll().toArray(new Category[(int)CategoryService.getInstance().count()]));
					insertCat.setEmptySelectionAllowed(false);
					insertCat.setEmptySelectionCaption("Choose Category");
					binder.forField(insertCat).asRequired("Every hotel must have a category").bind(Hotel::getCategory, Hotel::setCategory);
					update.addClickListener(up -> update("Category"));
					break;
				}
				case "Description" : {
					addComponents(insertText, buttons);
					insertText.setCaption("Insert Description");
					binder.forField(insertText).bind(Hotel::getDescription, Hotel::setDescription);
					update.addClickListener(up -> update("Description"));
					break;
				}
				case "URL" : {
					addComponents(insertText, buttons);
					insertText.setCaption("Insert URL");
					binder.forField(insertText).asRequired("Every hotel must have an url")
					.withValidator(new RegexpValidator("Url example: https://www.booking.com/.....html"
							, "^(http://|https://)www\\.booking\\.com/.+\\.html$"))
					.bind(Hotel::getUrl, Hotel::setUrl);
					update.addClickListener(up -> update("URL"));
					break;
				}
			}
		});
	}
	
	public void setHotels(Set<Hotel> hotels) {
		this.hotels = hotels;
		setCaption("You have chosen " + hotels.size() + " hotels");
	}
	
	private void update(String par) {
		binder.validate();
		System.out.println("binder is valid " + binder.isValid());
		if (binder.isValid()) {
			System.out.println("binderAfter is valid " + binder.isValid());
			for (Hotel hotel : hotels) {
				switch (chooseItem.getValue()) {
					case "Name" : hotel.setName(insertText.getValue());	break;
					case "Address" : hotel.setAddress(insertText.getValue()); break;
					case "Rating" : hotel.setRating(Integer.parseInt(insertPar.getValue())); break;
					case "Operates from" : hotel.setOperatesFrom(Duration.between(insertDate
							.getValue().atTime(0, 0), LocalDate.now().atTime(0, 0)).toDays()); break;
					case "Category" : hotel.setCategory(insertCat.getValue()); break;
					case "Description" : hotel.setDescription(insertText.getValue()); break;
					case "URL" : hotel.setUrl(insertText.getValue()); break;
				}
			}
			service.save(hotels);
			hotelView.updateListHotel();
			setVisible(false);
		}
	}
	
	private void removeComponentsIfExist() {
		removeComponent(insertText);
		removeComponent(insertPar);
		removeComponent(insertCat);
		removeComponent(insertDate);
	}
}
