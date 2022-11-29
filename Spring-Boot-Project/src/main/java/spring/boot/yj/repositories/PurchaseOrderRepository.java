package spring.boot.yj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.boot.yj.entities.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

}
