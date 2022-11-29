package spring.boot.yj.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "PRODUCT")
@Data
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id ;
	@NotBlank
	@Column(length = 255,name = "short_description")
	private String shortDesc;
	@NotBlank
	@Column(length = 255,name = "long_description")
	private String longDesc;
	@NotBlank
	@Column(length = 255)
	private String name;
	@NotBlank
	@Column(length = 255)
	private Double price;
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "inventory_id", nullable = false)
	private Inventory inventory;

}
