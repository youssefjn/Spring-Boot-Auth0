package spring.boot.yj.controllers;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.boot.yj.entities.PurchaseOrder;
import spring.boot.yj.entities.User;
import spring.boot.yj.service.PurchaseOrderService;

@RestController
@RequestMapping ("/order")
public class PurchaseOrderController {

	private PurchaseOrderService purchaseOrderService;

	public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {

		this.purchaseOrderService = purchaseOrderService;
	}

	@GetMapping

	public List<PurchaseOrder>getOrders(@AuthenticationPrincipal User user){
		return purchaseOrderService.getOrders(user);
	}
}
