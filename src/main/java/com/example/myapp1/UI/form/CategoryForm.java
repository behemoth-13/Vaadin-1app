package com.example.myapp1.UI.form;

import static com.example.myapp1.ElementId.*;

import java.util.Set;

import com.example.myapp1.UI.views.CategoryView;
import com.example.myapp1.dao.entity.Category;
import com.example.myapp1.service.CategoryService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class CategoryForm extends FormLayout{
	private static final long serialVersionUID = 1L;
	
	private TextField categoryField = new TextField("Category");
	private Button saveBtn = new Button("Save");
	private Button cancelBtn = new Button("Cancel");
	private final Label errorMessage = new Label("");
	
	private CategoryService service = CategoryService.getInstance();
	private Category category;
	private CategoryView categoryView;
	private Binder<Category> binder =new Binder<>(Category.class);
	
	public CategoryForm(CategoryView categoryView) {
		this.categoryView = categoryView;
		
		setSizeUndefined();
		errorMessage.setVisible(false);
		HorizontalLayout buttons = new HorizontalLayout(saveBtn, cancelBtn);
		errorMessage.setStyleName(ValoTheme.LABEL_FAILURE);
		addComponents(categoryField, buttons, errorMessage);
		
		saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveBtn.setClickShortcut(KeyCode.ENTER);
		saveBtn.addClickListener(e -> save());
		cancelBtn.addClickListener(e -> cancel());
		addListener(e -> errorMessage.setVisible(false));
		categoryField.addListener(e -> errorMessage.setVisible(false));
		categoryField.setDescription("This is Category");
		binder.forField(categoryField).asRequired("Hotel not null")
		.withValidator(categoryVal -> categoryVal.trim().length() > 1, "Category should not be empty")
		.bind(Category::getCategory, Category::setCategory);
		setIds();
	}

	public void setCategory(Category category) {
		try {
			this.category = category.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		binder.readBean(this.category);
		setVisible(true);
	}
	
	private void cancel() {
		binder.removeBean();
		setVisible(false);
	}
	
	public void delete(Set<Category> set) {
		for (Category category : set) {
			service.delete(category);
		}
		categoryView.updateListCategory();
		setVisible(false);
	}
	
	private void save() {
		try {
				binder.writeBean(category);
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		binder.validate();
		if (binder.isValid() && (!service.isExistCategory(category))) {
			
			service.save(category);
			categoryView.updateListCategory();
			setVisible(false);
		} else {
			binder.removeBean();
			errorMessage.setVisible(true);
			errorMessage.setValue("Category: " + category.getCategory() + " already exist!");
		}
	}
	
	private void setIds() {
		saveBtn.setId(CATEGORYFORM_SAVE_BTN);
		categoryField.setId(CATEGORYFORM_CATEGORYFIELD);
	}
}

