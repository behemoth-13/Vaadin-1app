package com.example.myapp1.UI.views;

import java.util.List;

import com.example.myapp1.UI.form.CategoryForm;
import com.example.myapp1.dao.entity.Category;
import com.example.myapp1.service.CategoryService;
import com.example.myapp1.service.HotelService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;

@SuppressWarnings("serial")
public class CategoryView extends VerticalLayout implements View {

	public static String VIEW_NAME="categories";
	private Grid<Category> gridCategory = new Grid<>(Category.class);
	private HotelService serviceHotel = HotelService.getInstance();//
	private CategoryService serviceCategory = CategoryService.getInstance();
	private CategoryForm formCategory = new CategoryForm(this);

	@Override
	public void enter(ViewChangeEvent event) {
		
		gridCategory.setSelectionMode(SelectionMode.MULTI);
        formCategory.setVisible(false);
        Button addCategoryBtn = new Button("New");
        addCategoryBtn.addClickListener(e -> {
        	gridCategory.asMultiSelect().clear();
        	formCategory.setCategory(new Category(null, ""));
        });
        
        Button editCategoryBtn = new Button("Edit");
        
        editCategoryBtn.addClickListener(e -> {
        	formCategory.setCategory(gridCategory.getSelectedItems().iterator().next());
        });
        
        Button deleteCategoryBtn = new Button("Delete");
        deleteCategoryBtn.addClickListener(e -> {
        	formCategory.delete(gridCategory.asMultiSelect().getSelectedItems());
        	gridCategory.asMultiSelect().clear();
        	//serviceCategory.refreshCategories();
        	serviceHotel.refreshHotels();
        });
        
        editCategoryBtn.setEnabled(false);
        deleteCategoryBtn.setEnabled(false);
        
        gridCategory.asMultiSelect().addValueChangeListener(e -> {
        	formCategory.setVisible(false);
        	editCategoryBtn.setEnabled(e.getValue().size() == 1);
	        deleteCategoryBtn.setEnabled(e.getValue().size() > 0);
        });
        
        HorizontalLayout toolbar = new HorizontalLayout(addCategoryBtn, editCategoryBtn, deleteCategoryBtn);
        HorizontalLayout category = new HorizontalLayout(gridCategory, formCategory);
        gridCategory.setSizeUndefined();
        
        gridCategory.asMultiSelect().clear();
        gridCategory.setColumns("category");
        
        addComponents(toolbar, category);
        
        updateListCategory();
		
	}
	
	public void updateListCategory() {
    	List<Category> categories = serviceCategory.findAll();
        gridCategory.setItems(categories);
        serviceHotel.refreshHotels();
	}
}
