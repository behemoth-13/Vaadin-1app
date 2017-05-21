package com.example.myapp1.UI;

import javax.servlet.annotation.WebServlet;

import com.example.myapp1.UI.views.CategoryView;
import com.example.myapp1.UI.views.HotelView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
public class MyUI extends UI {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_TITLE = "Demo: Hotels";
	private MenuBar menuBar;
	private Navigator navigator;
	private final Label selection = new Label("Choose item:");

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	getPage().setTitle(PAGE_TITLE);
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addComponents(selection, getMenuBar());
		Panel mainPanel = new Panel();
		mainLayout.addComponent(mainPanel);
		mainPanel.setSizeFull();
		setContent(mainLayout);
		setSizeFull();
		super.setId("mainView");
		mainPanel.setId("mainPanel");
		mainLayout.setId("mainLayout");
		mainLayout.setSizeFull();
		mainLayout.setExpandRatio(mainPanel, 100);
		mainLayout.setMargin(false);

    	navigator = new Navigator(this, mainPanel);
    	navigator.addView(HotelView.VIEW_NAME, HotelView.class);
    	navigator.addView(CategoryView.VIEW_NAME, CategoryView.class);
		navigator.navigateTo(HotelView.VIEW_NAME);
		
		setContent(mainLayout);
	}
	
	private MenuBar getMenuBar() {
		if (menuBar != null) {
			return menuBar;
		}
		menuBar = new MenuBar();
		//menuBar.setId("menuBar1");
		menuBar.addItem("Hotel", command -> navigator.navigateTo(HotelView.VIEW_NAME));
		menuBar.addItem("", null).setEnabled(false);
		menuBar.addItem("Categories", command -> navigator.navigateTo(CategoryView.VIEW_NAME));
				
		return menuBar;
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
    }
    
}
