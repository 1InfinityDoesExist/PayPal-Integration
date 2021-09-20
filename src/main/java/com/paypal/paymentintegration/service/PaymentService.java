package com.paypal.paymentintegration.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.paymentintegration.model.OrderDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentService {

	@Autowired
	private APIContext apiContext;

	public String authorizePayment(OrderDetail orderDetail) throws PayPalRESTException {

		log.info("-----Payment Service Class, authorizePayment method");
		Payer payer = getPayerInformation();
		RedirectUrls redirectUrls = getRedirectURLs();
		List<Transaction> listTransaction = getTransactionInformation(orderDetail);

		Payment requestPayment = new Payment();
		requestPayment.setTransactions(listTransaction);
		requestPayment.setRedirectUrls(redirectUrls);
		requestPayment.setPayer(payer);
		requestPayment.setIntent("authorize");

		Payment approvedPayment = requestPayment.create(apiContext);

		log.info("-----approvedPayment : {}", approvedPayment);
		return getApprovalLink(approvedPayment);

	}

	private Payer getPayerInformation() {

		log.info("-----Payment Service Class, getPayerInformation method");
		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		PayerInfo payerInfo = new PayerInfo();
		payerInfo.setFirstName("William").setLastName("Peterson").setEmail("william.peterso@gaian.com");

		payer.setPayerInfo(payerInfo);

		log.info("Payer : {}", payer);
		return payer;
	}

	private RedirectUrls getRedirectURLs() {

		log.info("-----Payment Service Class, getRedirectURLs method");
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("http://localhost:9090/cancel.html");
		redirectUrls.setReturnUrl("http://localhost:9090/review_payment");

		return redirectUrls;
	}

	private List<Transaction> getTransactionInformation(OrderDetail orderDetail) {

		log.info("-----Payment Service Class, getTransactionInformation method");

		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal(String.format("%.2f",
				new BigDecimal(orderDetail.getTotal()).setScale(2, RoundingMode.HALF_UP).doubleValue()));

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription(orderDetail.getProductName());

		List<Transaction> listTransaction = new ArrayList<>();
		listTransaction.add(transaction);

		log.info("ListTransaction : {}", listTransaction);
		return listTransaction;
	}

	/*
	 * This method parses the approved Payment object returned from PayPal to find
	 * the approval URL in JSON response
	 */
	private String getApprovalLink(Payment approvedPayment) {
		log.info("-----Payment Service Class, getApprovalLink method");
		List<Links> links = approvedPayment.getLinks();
		String approvalLink = null;

		for (Links link : links) {
			if (link.getRel().equalsIgnoreCase("approval_url")) {
				approvalLink = link.getHref();
				break;
			}
		}

		return approvalLink;
	}

	public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
		return Payment.get(apiContext, paymentId);
	}

	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {

		log.info("-----Payment Service Class, executePayment method");
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);

		Payment payment = new Payment().setId(paymentId);
		System.out.println(":Payment : " + payment);

		return payment.execute(apiContext, paymentExecution);
	}
}
