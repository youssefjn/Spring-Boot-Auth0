package spring.boot.yj.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "INVENTORY")
@Data
@NoArgsConstructor
public class Inventory {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToMany(mappedBy = "inventory", cascade = CascadeType.REMOVE, orphanRemoval = true)
	
	private List<Product> Products= new ArrayList<>();
	@NotBlank

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

}
