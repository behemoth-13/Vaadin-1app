package com.example.myapp1.UI.form;

import java.util.Set;

import com.example.myapp1.UI.MyUI;
import com.example.myapp1.dao.entity.Category;
import com.example.myapp1.service.CategoryService;
import com.vaadin.data.Binder;
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
	private Button save = new Button("Save");
	private Button cancel = new Button("Cancel");
	private final Label errorMessage = new Label("");
	
	private CategoryService service = CategoryService.getInstance();
	private Category category;
	private MyUI myUI;
	private Binder<Category> binder =new Binder<>(Category.class);
	
	public CategoryForm(MyUI myUI) {
		this.myUI = myUI;
		
		setSizeUndefined();
		errorMessage.setVisible(false);
		HorizontalLayout buttons = new HorizontalLayout(save, cancel);
		errorMessage.setStyleName(ValoTheme.LABEL_FAILURE);
		addComponents(categoryField, buttons, errorMessage);
		
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);
		
		save.addClickListener(e -> save());
		cancel.addClickListener(e -> cancel());
		addListener(e -> errorMessage.setVisible(false));
		categoryField.addListener(e -> errorMessage.setVisible(false));
		categoryField.setDescription("This is Category");
		binder.forField(categoryField).asRequired("Hotel not null")
		.withValidator(categoryVal -> categoryVal.trim().length() > 1, "Category should not be empty")
		.bind(Category::getCategory, Category::setCategory);
	}

	public void setCategory(Category category) {
		this.category = category;
		binder.setBean(category);
		setVisible(true);
		categoryField.selectAll();
		binder.validate();
	}
	
	private void cancel() {
		myUI.updateListCategory();
		service.refreshCategories();
		setVisible(false);
	}
	
	public void delete(Set<Category> set) {
		for (Category category : set) {
			service.delete(category);
		}
		myUI.updateListCategory();
		setVisible(false);
	}
	
	private void save() {
		binder.validate();
		if (binder.isValid() && (!service.isExistCategory(category))) {
			service.save(category);
			myUI.updateListCategory();
			setVisible(false);
		} else {
			errorMessage.setVisible(true);
			errorMessage.setValue("Category: " + category.getCategory() + " already exist!");
		}
	}
}

