package spring.boot.yj.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class Quantity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	@ManyToOne(optional = false)
	@JoinColumn(name = "purchaseOrder_id", nullable = false)
	private PurchaseOrder purchaseOrder;
}
