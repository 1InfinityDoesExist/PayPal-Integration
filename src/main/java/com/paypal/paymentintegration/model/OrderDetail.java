package com.paypal.paymentintegration.model;

import lombok.Data;

@Data
public class OrderDetail {
	private String productName;
	private Double subtotal;
	private String shipping;
	private Double tax;
	private Double total;

	public OrderDetail(String productName, Double subtotal, String shipping, Double tax, Double total) {
		this.productName = productName;
		this.subtotal = subtotal;
		this.shipping = shipping;
		this.tax = tax;
		this.total = total;
	}

}