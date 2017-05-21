package com.example.myapp1.UI.form;

import static com.example.myapp1.ElementId.*;

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
	
	private TextField nameField = new TextField("Name");
	private TextField addressField = new TextField("Address");
	private TextField ratingField = new TextField("Rating");
	private DateField operatesFromDateField = new DateField("Operates from");
	private NativeSelect<Category> categoryNativeSelect = new NativeSelect<>("Category");
	private TextArea descriptionField = new TextArea("Description");
	private TextField urlField = new TextField("Url");
	private PaymentField payment = new PaymentField();
	private Button saveBtn = new Button("Save");
	private Button cancelBtn = new Button("Cancel");
	private final Label errorMessage = new Label("");
	
	private HotelService service = HotelService.getInstance();
	private Hotel hotel;
	private HotelView hotelView;
	private Binder<Hotel> binder = new Binder<>(Hotel.class);
	
	public HotelForm(HotelView hotelView) {
		
		this.hotelView = hotelView;
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(saveBtn, cancelBtn);
		addComponents(nameField, addressField, ratingField, operatesFromDateField, categoryNativeSelect, descriptionField, urlField,
				payment, buttons, errorMessage);
		errorMessage.setVisible(false);
		errorMessage.setStyleName(ValoTheme.LABEL_FAILURE);
		
		categoryNativeSelect.setItems(CategoryService.getInstance().findAll().toArray(new Category[(int)CategoryService.getInstance().count()]));
		
		saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveBtn.setClickShortcut(KeyCode.ENTER);
		saveBtn.addClickListener(e -> save());
		cancelBtn.setClickShortcut(KeyCode.ESCAPE);
		cancelBtn.addClickListener(e -> cancel());

		nameField.addListener(e -> errorMessage.setVisible(false));
		addressField.addListener(e -> errorMessage.setVisible(false));
		ratingField.addListener(e -> errorMessage.setVisible(false));
		operatesFromDateField.addListener(e -> errorMessage.setVisible(false));
		categoryNativeSelect.addListener(e -> errorMessage.setVisible(false));
		descriptionField.addListener(e -> errorMessage.setVisible(false));
		urlField.addListener(e -> errorMessage.setVisible(false));
		addListener(e -> errorMessage.setVisible(false));
	
		payment.addValueChangeListener(event -> Notification.show(payment.getNotification(),
                Type.TRAY_NOTIFICATION));
		
		setToolTips();
		bindFields();
		setIds();
	}
	
	public void refreshCategoryField() {
		categoryNativeSelect.clear();
		categoryNativeSelect.setItems(CategoryService.getInstance().findAll().toArray(new Category[(int)CategoryService.getInstance().count()]));
	}
	
	private void bindFields() {
		binder.forField(nameField).asRequired("Every hotel must have a name")
		.withValidator(nameVal -> nameVal.trim().length() > 1, "Name should not be empty")
		.bind(Hotel::getName, Hotel::setName);
		
		binder.forField(addressField).asRequired("Every hotel must have an address")
		.withValidator(addressVal -> addressVal.trim().length() > 1, "Address should not be empty")
		.bind(Hotel::getAddress, Hotel::setAddress);
		
		binder.forField(ratingField).withConverter(new StringToIntegerConverter(0, "Only Digits!"))
		.asRequired("Every hotel must have a rating")
		.withValidator(ratingVal -> ratingVal < 6, "Rating should be a number less or 5")
		.withValidator(ratingVal -> ratingVal > 0, "Rating should be a positive number")
		.bind(Hotel::getRating, Hotel::setRating);
		
		binder.forField(categoryNativeSelect).asRequired("Every hotel must have a category").bind(Hotel::getCategory, Hotel::setCategory);
		
		binder.forField(operatesFromDateField).withConverter(new DateConverter())
		.asRequired("Every hotel must have a date since it works")
		.withValidator(operatesFromVal -> operatesFromVal < 10950, "Date isn't more senior than 30 years")
		.bind(Hotel::getOperatesFrom, Hotel::setOperatesFrom);
		
		binder.forField(descriptionField).bind(Hotel::getDescription, Hotel::setDescription);
		
		binder.forField(urlField).asRequired("Every hotel must have an url")
		.withValidator(new RegexpValidator("Url example: https://www.booking.com/.....html", "^(http://|https://)www\\.booking\\.com/.+\\.html$"))
		.bind(Hotel::getUrl, Hotel::setUrl);
		
		binder.forField(payment).bind(Hotel::getPayment, Hotel::setPayment);
	}
	
	private void setToolTips() {
		//for fields
		nameField.setDescription("Hotel's name");
		addressField.setDescription("Hotel's address");
		ratingField.setDescription("Rating of hotel");
		operatesFromDateField.setDescription("Date since it works");
		categoryNativeSelect.setDescription("Category of hotel");
		descriptionField.setDescription("Description of hotel");
		urlField.setDescription("Url of hotel");
		payment.setDescription("Field of payment");
		//for buttons
		saveBtn.setDescription("Save");
		cancelBtn.setDescription("Cancel");
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
	
	private void setIds() {
		nameField.setId(HOTELFORM_NAME_FIELD);
		addressField.setId(HOTELFORM_ADDRESS_FIELD);
		ratingField.setId(HOTELFORM_RATING_FIELD);
		operatesFromDateField.setId(HOTELFORM_OPERATESFROM_DATE_FIELD);
		categoryNativeSelect.setId(HOTELFORM_CATEGORY_NATIVE_SELECT);
		descriptionField.setId(HOTELFORM_DESCRIPTION_FIELD);
		urlField.setId(HOTELFORM_URL_FIELD);
		saveBtn.setId(HOTELFORM_SAVE_BTN);
	}
}
