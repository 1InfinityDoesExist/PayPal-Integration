package com.paypal.paymentintegration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.base.rest.PayPalRESTException;
import com.paypal.paymentintegration.model.OrderDetail;
import com.paypal.paymentintegration.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/authorize_payment")
public class AutorizeController {

	@Autowired
	private PaymentService paymentServices;

	@PostMapping
	public ResponseEntity<?> authorzePayment(@RequestBody OrderDetail orderDetails) throws PayPalRESTException {
		log.info("-----Authorize Payment Api Calling");

		String approvalLink = paymentServices.authorizePayment(orderDetails);

		return ResponseEntity.status(HttpStatus.OK).body(approvalLink);

	}

}
