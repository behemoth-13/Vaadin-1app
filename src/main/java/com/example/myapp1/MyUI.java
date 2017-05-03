package com.example.myapp1;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.example.myapp1.hotel.Hotel;
import com.example.myapp1.hotel.HotelService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
	
	private HotelService service = HotelService.getInstance();
	private Grid<Hotel> grid = new Grid<>(Hotel.class);
	private TextField filterByName = new TextField();
	private TextField filterByAddress = new TextField();
	private HotelForm form = new HotelForm(this);

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        filterByName.setPlaceholder("filter by name...");
        filterByName.addValueChangeListener(e -> updateList());
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button clearFilterByNameBtn = new Button(VaadinIcons.CLOSE);
        clearFilterByNameBtn.setDescription("Clear the current filter");
        clearFilterByNameBtn.addClickListener(e -> filterByName.clear());
        
        filterByAddress.setPlaceholder("filter by address...");
        filterByAddress.addValueChangeListener(e -> updateList());
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
        
        Button addHotelBtn = new Button("Add new hotel");
        addHotelBtn.addClickListener(e -> {
        	grid.asSingleSelect().clear();
        	form.setHotel(new Hotel());
        });
        
        HorizontalLayout toolbar = new HorizontalLayout(filteringByName, filteringByAdress, addHotelBtn);
        
        grid.setColumns("name", "address", "rating", "operatesFrom", "category");

        Column<Hotel, String> htmlColumn = grid.addColumn(hotel ->
        "<a href='" + hotel.getUrl() + "' target='_top'>" + hotel.getUrl() +"</a>",
        new HtmlRenderer()).setCaption("Url");
        
        grid.addColumn("description");
        
        
        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);
        
        layout.addComponents(toolbar, main);
        
        updateList();
        
        setContent(layout);
        
        form.setVisible(false);
        
        grid.asSingleSelect().addValueChangeListener(event -> {
        	if(event.getValue() == null) {
        		form.setVisible(false);
        	} else {
        		form.setHotel(event.getValue());
        	}
        });
    }

	public void updateList() {
		List<Hotel> hotels = service.findAll(filterByName.getValue(), filterByAddress.getValue());
        grid.setItems(hotels);
	}

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
