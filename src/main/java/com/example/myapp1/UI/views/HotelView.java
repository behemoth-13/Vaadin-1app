package com.example.myapp1.UI.views;

import static com.example.myapp1.ElementId.*;

import java.util.List;
import java.util.Set;

import com.example.myapp1.UI.form.BulkForm;
import com.example.myapp1.UI.form.HotelForm;
import com.example.myapp1.dao.entity.Hotel;
import com.example.myapp1.dao.entity.Payment;
import com.example.myapp1.service.CategoryService;
import com.example.myapp1.service.HotelService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.TextRenderer;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class HotelView extends VerticalLayout implements View {

	public static String VIEW_NAME = "hotels";
	private HotelService serviceHotel = HotelService.getInstance();
	private CategoryService serviceCategory = CategoryService.getInstance();
	private Grid<Hotel> gridHotel = new Grid<>(Hotel.class);
	private TextField filterByName = new TextField();
	private TextField filterByAddress = new TextField();
	private HotelForm formHotel = new HotelForm(this);
	private BulkForm formBulk = new BulkForm(this);
	private PopupView popup = new PopupView(null, formBulk);

	@Override
	public void enter(ViewChangeEvent event) {
		
        filterByName.setPlaceholder("filter by name...");
        filterByName.addValueChangeListener(e -> updateListHotel());
        filterByName.setValueChangeMode(ValueChangeMode.EAGER);
        
        Button clearFilterByNameBtn = new Button(VaadinIcons.CLOSE);
        clearFilterByNameBtn.setDescription("Clear the current filter");
        clearFilterByNameBtn.addClickListener(e -> filterByName.clear());
        
        filterByAddress.setPlaceholder("filter by address...");
        filterByAddress.addValueChangeListener(e -> updateListHotel());
        filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button clearFilterByAddressBtn = new Button(VaadinIcons.CLOSE);
        clearFilterByAddressBtn.setDescription("Clear the current filter");
        clearFilterByAddressBtn.addClickListener(e -> filterByAddress.clear());
        
        CssLayout filteringByName = new CssLayout();
        filteringByName.addComponents(filterByName, clearFilterByNameBtn);
        filteringByName.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        
        CssLayout filteringByAdress = new CssLayout();
        filteringByAdress.addComponents(filterByAddress, clearFilterByAddressBtn);
        filteringByAdress.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        
        Button addHotelBtn = new Button("New");
        addHotelBtn.setId(HOTELVIEW_ADDHOTEL_BTN);
        
        Button editHotelBtn = new Button("Edit");
        
        Button deleteHotelBtn = new Button("Delete");
        
        Button bulkUpdateHotelBtn = new Button("Bulk Update");
        
        gridHotel.setSelectionMode(SelectionMode.MULTI);
        
        addHotelBtn.addClickListener(e -> {
        	gridHotel.asMultiSelect().clear();
        	formHotel.setHotel(new Hotel(null, "", "", 0, 0L, serviceCategory.getDefault(), "", new Payment()));
        });

        popup.setHideOnMouseOut(false);
        
        deleteHotelBtn.addClickListener(e -> {
        	delete(gridHotel.asMultiSelect().getSelectedItems());
        });
        
        bulkUpdateHotelBtn.addClickListener(e -> {
        	popup.setPopupVisible(true);
        	formBulk.setVisible(true);
        	formBulk.setHotels(gridHotel.asMultiSelect().getSelectedItems());
        });
        
        editHotelBtn.addClickListener(e -> {
        	formHotel.setHotel(gridHotel.asMultiSelect().getSelectedItems().iterator().next());
        });
        
        HorizontalLayout toolbar = new HorizontalLayout(filteringByName, filteringByAdress, addHotelBtn, editHotelBtn, deleteHotelBtn, bulkUpdateHotelBtn, popup);
        
        gridHotel.setColumns("name", "address", "rating", "operatesFrom", "category");
        gridHotel.getColumn("category").setRenderer(new TextRenderer("Category was deleted"));
        gridHotel.addColumn(hotel ->
        "<a href='" + hotel.getUrl() + "' target='_blank'>" + hotel.getUrl() +"</a>",
        new HtmlRenderer()).setCaption("Url");
        
        gridHotel.addColumn("description");
        
        HorizontalLayout main = new HorizontalLayout(gridHotel, formHotel);
        main.setSizeFull();
        gridHotel.setSizeFull();
        main.setExpandRatio(gridHotel, 1);
        
        addComponents(toolbar, main);
        
        updateListHotel();
        
        formHotel.setVisible(false);
        
        bulkUpdateHotelBtn.setEnabled(false);
		editHotelBtn.setEnabled(false);
		deleteHotelBtn.setEnabled(false);
        
        gridHotel.asMultiSelect().addValueChangeListener(e -> {
        	formHotel.setVisible(false);
        	bulkUpdateHotelBtn.setEnabled(e.getValue().size() > 1);
        	editHotelBtn.setEnabled(e.getValue().size() == 1);
        	deleteHotelBtn.setEnabled(e.getValue().size() > 0);
        });
	}

	public void updateListHotel() {
		List<Hotel> hotels = serviceHotel.findAll();
        gridHotel.setItems(hotels);
        serviceHotel.refreshHotels();
        formHotel.refreshCategoryField();
	}
	
	public void delete(Set<Hotel> set) {
		for (Hotel hotel : set) {
			serviceHotel.delete(hotel);
		}
		updateListHotel();
	}
}