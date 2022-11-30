package spring.boot.yj.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.boot.yj.entities.PurchaseOrder;
import spring.boot.yj.entities.User;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
	 List<PurchaseOrder> findAllByUser(User user);
}
