package com.example.myapp1.UI.form;

import com.example.myapp1.UI.form.converter.DateConverter;
import com.example.myapp1.UI.views.HotelView;
import com.example.myapp1.dao.entity.Category;
import com.example.myapp1.dao.entity.Hotel;
import com.example.myapp1.service.CategoryService;
import com.example.myapp1.service.HotelService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
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
	private PaymentField payment = new PaymentField();
	private Button save = new Button("Save");
	private Button cancel = new Button("Cancel");
	private final Label errorMessage = new Label("");
	
	private HotelService service = HotelService.getInstance();
	private Hotel hotel;
	private HotelView hotelView;
	private Binder<Hotel> binder = new Binder<>(Hotel.class);
	
	public HotelForm(HotelView hotelView) {
		
		this.hotelView = hotelView;
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(save, cancel);
		addComponents(name, address, rating, operatesFrom, category, description, url,
				payment, buttons, errorMessage);
		errorMessage.setVisible(false);
		errorMessage.setStyleName(ValoTheme.LABEL_FAILURE);
		
		category.setItems(CategoryService.getInstance().findAll().toArray(new Category[(int)CategoryService.getInstance().count()]));
		
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);
		save.addClickListener(e -> save());
		cancel.setClickShortcut(KeyCode.ESCAPE);
		cancel.addClickListener(e -> cancel());

		name.addListener(e -> errorMessage.setVisible(false));
		address.addListener(e -> errorMessage.setVisible(false));
		rating.addListener(e -> errorMessage.setVisible(false));
		operatesFrom.addListener(e -> errorMessage.setVisible(false));
		category.addListener(e -> errorMessage.setVisible(false));
		description.addListener(e -> errorMessage.setVisible(false));
		url.addListener(e -> errorMessage.setVisible(false));
		addListener(e -> errorMessage.setVisible(false));
	
		payment.addValueChangeListener(event -> Notification.show(payment.getNotification(),
                Type.TRAY_NOTIFICATION));
		
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
		.withValidator(ratingVal -> ratingVal < 6, "Rating should be a number less or 5")
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
		
		binder.forField(payment).bind(Hotel::getPayment, Hotel::setPayment);
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
		payment.setDescription("Field of payment");
		//for buttons
		save.setDescription("Save");
		cancel.setDescription("Cancel");
	}

	public void setHotel(Hotel hotel) {
		try {
			this.hotel = hotel.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		binder.readBean(this.hotel);
		setVisible(true);
		binder.validate();
	}
	
	private void save() {
		try {
			binder.writeBean(hotel);
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		binder.validate();
		if (binder.isValid() && (!service.isExistHotel(hotel))) {
			hotel.setPayment(payment.getValue());
			service.save(hotel);
			hotelView.updateListHotel();
			setVisible(false);
		}else {
			binder.removeBean();
			errorMessage.setVisible(true);
			errorMessage.setValue("Hotel exist!");
		}
	}
	
	private void cancel() {
		binder.removeBean();
		setVisible(false);
	}
}
