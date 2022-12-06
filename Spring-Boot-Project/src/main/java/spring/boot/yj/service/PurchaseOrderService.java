package spring.boot.yj.service;

import java.util.List;

import org.springframework.stereotype.Service;

import spring.boot.yj.entities.PurchaseOrder;
import spring.boot.yj.entities.User;
import spring.boot.yj.repositories.PurchaseOrderRepository;

@Service

public class PurchaseOrderService {
	private PurchaseOrderRepository purchaseOrderRepository;

	public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository) {
		this.purchaseOrderRepository = purchaseOrderRepository;
	}
	public List<PurchaseOrder> getOrders(User user){
		return purchaseOrderRepository.findAllByUser(user);
	}

}
