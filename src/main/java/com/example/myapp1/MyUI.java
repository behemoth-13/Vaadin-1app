package com.example.myapp1;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.example.myapp1.category.Category;
import com.example.myapp1.category.CategoryForm;
import com.example.myapp1.category.CategoryService;
import com.example.myapp1.hotel.Hotel;
import com.example.myapp1.hotel.HotelService;
import com.example.myapp1.hotel.HotelForm;
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
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Theme("mytheme")
public class MyUI extends UI {
	
	private HotelService serviceHotel = HotelService.getInstance();
	private CategoryService serviceCategory = CategoryService.getInstance();
	private Grid<Hotel> gridHotel = new Grid<>(Hotel.class);
	private Grid<Category> gridCategory = new Grid<>(Category.class);
	private TextField filterByName = new TextField();
	private TextField filterByAddress = new TextField();
	private HotelForm formHotel = new HotelForm(this);
	private CategoryForm formCategory = new CategoryForm(this);
	private MenuBar barmenu = new MenuBar();
	private final Label selection = new Label("Choose item:");

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	
        final VerticalLayout layout = new VerticalLayout();
    	
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
        
        Button addHotelBtn = new Button("Add new hotel");
        addHotelBtn.addClickListener(e -> {
        	gridHotel.asSingleSelect().clear();
        	formHotel.setHotel(new Hotel(null, "", "", 0, 0L, serviceCategory.getDefault(), ""));
        });
        
        HorizontalLayout toolbar = new HorizontalLayout(filteringByName, filteringByAdress, addHotelBtn);
        
        gridHotel.setColumns("name", "address", "rating", "operatesFrom", "category");

        Column<Hotel, String> htmlColumn = gridHotel.addColumn(hotel ->
        "<a href='" + hotel.getUrl() + "' target='_blank'>" + hotel.getUrl() +"</a>",
        new HtmlRenderer()).setCaption("Url");
        
        gridHotel.addColumn("description");
        
        HorizontalLayout main = new HorizontalLayout(gridHotel, formHotel);
        main.setSizeFull();
        gridHotel.setSizeFull();
        main.setExpandRatio(gridHotel, 1);
        
        layout.addComponents(selection, barmenu, toolbar, main);
        
        updateListHotel();
        
        setContent(layout);
        
        formHotel.setVisible(false);
        
        gridHotel.asSingleSelect().addValueChangeListener(event -> {
        	if(event.getValue() == null) {
        		formHotel.setVisible(false);
        	} else {
        		formHotel.setHotel(event.getValue());
        	}
        });
        
		MenuBar.Command showCategories = new MenuBar.Command() {
    	    MenuItem previous = null;
    	    @Override
    	    public void menuSelected(MenuItem selectedItem) {
    	    	
    	        selection.setValue("Ordered a " +
    	                selectedItem.getText() +
    	                " from menu.");

    	        if (previous != null)
    	            previous.setStyleName(null);
    	        selectedItem.setStyleName("highlight");
    	        previous = selectedItem;
    	        
    	        gridCategory.setSelectionMode(SelectionMode.MULTI);
    	        formCategory.setVisible(false);
    	        Button addCategoryBtn = new Button("New");
    	        addCategoryBtn.addClickListener(e -> {
    	        	gridCategory.asMultiSelect().clear();
    	        	formCategory.setCategory(new Category(null, ""));
    	        });
    	        
    	        Button editCategoryBtn = new Button("Edit");
    	        editCategoryBtn.addClickListener(event -> {
    	        	formCategory.setCategory(gridCategory.asMultiSelect().getSelectedItems().iterator().next());
    	        });
    	        
    	        Button deleteCategoryBtn = new Button("Delete");
    	        deleteCategoryBtn.addClickListener(e -> {
    	        	formCategory.delete(gridCategory.asMultiSelect().getSelectedItems());
    	        	gridCategory.asMultiSelect().clear();
    	        	formHotel.refreshCategoryField();
    	        });
    	        
    	        addCategoryBtn.setVisible(true);
    	        editCategoryBtn.setVisible(false);
    	        deleteCategoryBtn.setVisible(false);
    	        
    	        gridCategory.asMultiSelect().addValueChangeListener(event -> {
    	        	if(event.getValue().size() == 1) {
    	        		editCategoryBtn.setVisible(true);
    	        		deleteCategoryBtn.setVisible(true);
    	        	} else if(event.getValue().size() == 0) {
    	        		editCategoryBtn.setVisible(false);
    	        		deleteCategoryBtn.setVisible(false);
    	        	}
    	        	else if(event.getValue().size() > 1) {
    	        		editCategoryBtn.setVisible(false);
    	        		deleteCategoryBtn.setVisible(true);
    	        	}
    	        });
    	        
    	        HorizontalLayout toolbar = new HorizontalLayout(addCategoryBtn, editCategoryBtn, deleteCategoryBtn);
    	        HorizontalLayout category = new HorizontalLayout(gridCategory, formCategory);
    	        gridCategory.setSizeUndefined();
    	        
		        gridCategory.asMultiSelect().clear();
		        gridCategory.setColumns("category");
    	        
    	        layout.removeAllComponents();
    	        layout.addComponents(selection, barmenu, toolbar, category);
    	        
    	        updateListCategory();
    	    }
    	};
    	
		MenuBar.Command showHotels = new MenuBar.Command() {
    	    MenuItem previous = null;
    	    @Override
    	    public void menuSelected(MenuItem selectedItem) {
    	        selection.setValue("Ordered a " +
    	                selectedItem.getText() +
    	                " from menu.");

    	        if (previous != null)
    	            previous.setStyleName(null);
    	        selectedItem.setStyleName("highlight");
    	        previous = selectedItem;
    	        layout.removeAllComponents();
    	        layout.addComponents(selection, barmenu, toolbar, main);
    	        
    	        updateListHotel();
    	    }
    	};
    			
    	barmenu.addItem("Hotel List", VaadinIcons.BUILDING, showHotels);
    	barmenu.addItem("Hotel Categories", VaadinIcons.STAR, showCategories);
    }

    public void updateListCategory() {
    	List<Category> categories = serviceCategory.findAll();
        gridCategory.setItems(categories);
        formHotel.refreshCategoryField();
        serviceHotel.updateCategory();
	}

	public void updateListHotel() {
		List<Hotel> hotels = serviceHotel.findAll(filterByName.getValue(), filterByAddress.getValue());
        gridHotel.setItems(hotels);
	}

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
