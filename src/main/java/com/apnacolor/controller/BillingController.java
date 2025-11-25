package com.apnacolor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apnacolor.entity.Bill;
import com.apnacolor.entity.BillDetails;
import com.apnacolor.repository.BillDetailsRepository;
import com.apnacolor.repository.BillRepository;
import com.apnacolor.request.BillResponse;
import com.apnacolor.request.BillingRequest;
import com.apnacolor.services.BillingServiceImpl;


@RestController
@RequestMapping("/Billing")
//@CrossOrigin(origins = "http://localhost:4200")
public class BillingController {

	@Autowired
	private BillingServiceImpl billingService;
	
	@Autowired
	private BillDetailsRepository billRepository;
	@PostMapping("/api/bill")
	public ResponseEntity<Bill> createBill(@RequestBody BillingRequest request) {
	    Bill bill = billingService.createBillFromCart(request.getCartIds(), request.getPaymentMethod());
	    System.out.println("Received cart IDs: " + request.getCartIds());
	    System.out.println(bill.toString());
	    return ResponseEntity.ok(bill);
	}
	
	@GetMapping("/api/bill/{billId}")
	public ResponseEntity<BillResponse> getInvoice(@PathVariable Long billId) {
	    BillResponse dto = billingService.getBillDtoById(billId);
	    return ResponseEntity.ok(dto);
	}





	
}
