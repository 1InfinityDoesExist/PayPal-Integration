package com.paypal.paymentintegration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.paymentintegration.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/execute_payment")
public class ExecutePayment {

	@Autowired
	private PaymentService paymentServices;

	@GetMapping("/{paymentId}/{payerId}")
	public ResponseEntity<?> executePayment(@PathVariable String paymentId, @PathVariable String payerId)
			throws PayPalRESTException {

		log.info("-----Execute Payment Class, executePayment method");

		System.out.println(paymentId);
		System.out.println(payerId);
		Payment payment = paymentServices.executePayment(paymentId, payerId);

		PayerInfo payerInfo = payment.getPayer().getPayerInfo();
		Transaction transaction = payment.getTransactions().get(0);

		System.out.println("::::::::::::::::::::::" + transaction);
		return ResponseEntity.status(HttpStatus.OK).body("Chal gaya");
	}
}
