package com.example.myapp1.hotel;

import com.example.myapp1.DateConverter;
import com.example.myapp1.MyUI;
import com.example.myapp1.category.Category;
import com.example.myapp1.category.CategoryService;
import com.example.myapp1.hotel.Hotel;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class HotelForm extends FormLayout{
	private static final long serialVersionUID = 1L;
	
	private TextField name = new TextField("Name");
	private TextField address = new TextField("Address");
	private TextField rating = new TextField("Rating");
	private DateField operatesFrom = new DateField("Operates from");
	private NativeSelect<Category> category = new NativeSelect<>("Category");
	private TextArea description = new TextArea("Description");
	private TextField url = new TextField("Url");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");
	
	private HotelService service = HotelService.getInstance();
	private Hotel hotel;
	private MyUI myUI;
	private Binder<Hotel> binder =new Binder<>(Hotel.class);
	
	public HotelForm(MyUI myUI) {
		this.myUI = myUI;
		
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(save, delete);
		addComponents(name, address, rating, operatesFrom, category, description, url, buttons);
		
		category.setItems(CategoryService.getInstance().findAll().toArray(new Category[(int)CategoryService.getInstance().count()]));
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);
		
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());

		setToolTips();
		bindFields();
	}
	
	public void refreshCategoryField() {
		category.clear();
		category.setItems(CategoryService.getInstance().findAll().toArray(new Category[(int)CategoryService.getInstance().count()]));
	}
	
	private void bindFields() {
		binder.forField(name).asRequired("Every hotel must have a name")
		.withValidator(nameVal -> nameVal.trim().length() > 1, "Name should not be empty")
		.bind(Hotel::getName, Hotel::setName);
		
		binder.forField(address).asRequired("Every hotel must have an address")
		.withValidator(addressVal -> addressVal.trim().length() > 1, "Address should not be empty")
		.bind(Hotel::getAddress, Hotel::setAddress);
		
		binder.forField(rating).withConverter(new StringToIntegerConverter(0, "Only Digits!"))
		.asRequired("Every hotel must have an address")
		.withValidator(ratingVal -> ratingVal < 6, "Rating should be a number less then 6")
		.withValidator(ratingVal -> ratingVal > 0, "Rating should be a positive number")
		.bind(Hotel::getRating, Hotel::setRating);
		
		binder.forField(category).asRequired("Every hotel must have a category").bind(Hotel::getCategory, Hotel::setCategory);
		
		binder.forField(operatesFrom).withConverter(new DateConverter())
		.asRequired("Every hotel must have a date since it works")
		.withValidator(operatesFromVal -> operatesFromVal < 10950, "Date isn't more senior than 30 years")
		.bind(Hotel::getOperatesFrom, Hotel::setOperatesFrom);
		
		binder.forField(description).bind(Hotel::getDescription, Hotel::setDescription);
		
		binder.forField(url).asRequired("Every hotel must have an url")
		.withValidator(new RegexpValidator("Url example: https://www.booking.com/.....html", "^(http://|https://)www\\.booking\\.com/.+\\.html$"))
		.bind(Hotel::getUrl, Hotel::setUrl);
	}
	
	private void setToolTips() {
		name.setDescription("Hotel's name");
		address.setDescription("Hotel's address");
		rating.setDescription("Rating of hotel");
		operatesFrom.setDescription("Date since it works");
		category.setDescription("Category of hotel");
		description.setDescription("Description of hotel");
		url.setDescription("Url of hotel");
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
		binder.setBean(hotel);
		delete.setVisible(hotel.isPersisted());
		setVisible(true);
		name.selectAll();
		binder.validate();
	}
	
	private void delete() {
		service.delete(hotel);
		myUI.updateListHotel();
		setVisible(false);
	}
	
	private void save() {
		binder.validate();
		if (binder.isValid()) {
			service.save(hotel);
			myUI.updateListHotel();
			setVisible(false);
		}
	}
}
