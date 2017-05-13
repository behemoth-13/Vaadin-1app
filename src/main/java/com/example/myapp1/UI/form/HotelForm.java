package com.example.myapp1.UI.form;

import com.example.myapp1.UI.MyUI;
import com.example.myapp1.UI.form.converter.DateConverter;
import com.example.myapp1.dao.entity.Category;
import com.example.myapp1.dao.entity.Hotel;
import com.example.myapp1.service.CategoryService;
import com.example.myapp1.service.HotelService;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class HotelForm extends FormLayout{
	private static final long serialVersionUID = 1L;
	
	private TextField name = new TextField("Name");
	private TextField address = new TextField("Address");
	private TextField rating = new TextField("Rating");
	private DateField operatesFrom = new DateField("Operates from");
	private NativeSelect<Category> category = new NativeSelect<>("Category");
	private TextArea description = new TextArea("Description");
	private TextField url = new TextField("Url");
	private Button save = new Button(VaadinIcons.CHECK);
	private Button delete = new Button(VaadinIcons.TRASH);
	private Button cancel = new Button(VaadinIcons.CLOSE);
	
	private HotelService service = HotelService.getInstance();
	private Hotel hotel;
	private MyUI myUI;
	private Binder<Hotel> binder =new Binder<>(Hotel.class);
	
	public HotelForm(MyUI myUI) {
		this.myUI = myUI;
		
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(save, delete, cancel);
		addComponents(name, address, rating, operatesFrom, category, description, url, buttons);
		
		category.setItems(CategoryService.getInstance().findAll().toArray(new Category[(int)CategoryService.getInstance().count()]));
		//save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		//save.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		save.setClickShortcut(KeyCode.ENTER);
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());
		cancel.setClickShortcut(KeyCode.ESCAPE);
		cancel.addClickListener(e -> cancel());

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
		.asRequired("Every hotel must have a rating")
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
		//for fields
		name.setDescription("Hotel's name");
		address.setDescription("Hotel's address");
		rating.setDescription("Rating of hotel");
		operatesFrom.setDescription("Date since it works");
		category.setDescription("Category of hotel");
		description.setDescription("Description of hotel");
		url.setDescription("Url of hotel");
		//for buttons
		save.setDescription("Save");
		delete.setDescription("Delete");
		cancel.setDescription("Cancel");
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
		if (binder.isValid() && (!service.isExistHotel(hotel))) {
			System.out.println("in save");
			service.save(hotel);
			myUI.updateListHotel();
			setVisible(false);
		}
	}
	
	private void cancel() {
		myUI.updateListCategory();
		service.refreshHotels();
		setVisible(false);
	}
}
