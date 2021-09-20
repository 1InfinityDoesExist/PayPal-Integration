package com.paypal.paymentintegration.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.paymentintegration.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/review_payment")
public class ReviewPayment {

	@Autowired
	private PaymentService paymentServices;

	@GetMapping
	public ResponseEntity<?> reviewPayment(HttpServletRequest request) throws PayPalRESTException {

		String paymentId = request.getParameter("paymentId");
		String payerId = request.getParameter("PayerID");

		System.out.println(payerId);
		System.out.println(paymentId);
		log.info("----Inside ReviewPayment Class, reviewPayment method");
		Payment payment = paymentServices.getPaymentDetails(paymentId);

		log.info("----Payment : {}", payment);

		PayerInfo payerInfo = payment.getPayer().getPayerInfo();
		Transaction transaction = payment.getTransactions().get(0);
		ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();

		return ResponseEntity.status(HttpStatus.OK).body("hello");

	}

}
