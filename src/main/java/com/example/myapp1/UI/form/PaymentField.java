package com.example.myapp1.UI.form;

import static com.example.myapp1.ElementId.*;

import java.util.Arrays;
import java.util.List;

import com.example.myapp1.dao.entity.Payment;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PaymentField extends CustomField<Payment> {
	private static final long serialVersionUID = 1L;
	
	private static final String CREDIT_CARD = "CreditCard";
	private static final String CASH = "Cash";
	
	private List<String> data = Arrays.asList(CREDIT_CARD, CASH);
	private RadioButtonGroup<String> radioButtons = new RadioButtonGroup<>(null, data);
	private TextField depositField = new TextField();
	private Label cashMessage = new Label("Payment will be made\ndirectly in the hotel" , ContentMode.PREFORMATTED);
	
	
	private Binder<Payment> binder = new Binder<>(Payment.class);
	private Payment payment;
	private String notification;

	@Override
	public Payment getValue() {
		return payment;
	}

	@Override
	protected Component initContent() {
		VerticalLayout ver = new VerticalLayout();
		radioButtons.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		cashMessage.addStyleName(ValoTheme.LABEL_SMALL);
		ver.setMargin(false);
		setCaption("Payment");
		ver.addComponents(radioButtons, depositField, cashMessage);

		radioButtons.addValueChangeListener(e -> {
			switch (e.getValue()) {
				case CREDIT_CARD : {
					if (payment.getDeposit() != null) {
						depositField.setValue(payment.getDeposit().toString());
						setValue(payment);
					} else {
						depositField.setValue("0");
						setValue(new Payment(0));
					}
					break;
				}
				case CASH : {
					if (payment.getDeposit() != null) {
						setValue(new Payment(null));
					}
					break;
				}
			}
			depositField.setVisible(e.getValue() == CREDIT_CARD);
			cashMessage.setVisible(e.getValue() == CASH);
			
		});
		
		depositField.addValueChangeListener(e -> {
			if (!e.getValue().isEmpty() && binder.isValid())
				setValue(new Payment(Integer.parseInt(e.getValue())));
		});
			
			
		
		binder.forField(depositField).withConverter(new StringToIntegerConverter(0, "Only Digits!"))
		.withValidator(r -> r >= 0, "0% is min deposit")
		.withValidator(r -> r <= 100, "100% is max deposit")
		.bind(Payment::getDeposit, Payment::setDeposit);
		setIds();
		return ver;
	}

	@Override
	protected void doSetValue(Payment payment) {
		updateNotification(this.payment, payment);
		this.payment = new Payment(payment.getDeposit());
		
		if (payment.getDeposit() == null) {
			radioButtons.setSelectedItem(data.get(1));
		} else {
			binder.setBean(payment);
			radioButtons.setSelectedItem(data.get(0));
		}
	}
	
	private void updateNotification(Payment oldValue, Payment value) {
		if (oldValue == null) { 
			notification = "Change payment field and see the difference";
		} else {
			notification = "Earlier " + getNotifMessage(oldValue) + ", " + "now " + getNotifMessage(value);
		}
	}
	
	private String getNotifMessage(Payment value) {
		if (value.getDeposit() == null) {
			return "you choose cash";
		} else {
			return "you choose deposit " + value.getDeposit() + "%";
		}
	}
	
	public String getNotification() {
		return notification;
	}
	
	private void setIds() {
		radioButtons.setId(PAYMENTFIELD_RADIO_BUTTONS);
		depositField.setId(PAYMENTFIELD_DEPOSIT_FIELD);
	}
}