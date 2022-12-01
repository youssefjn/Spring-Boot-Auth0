package spring.boot.yj.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table (name = "PURCHASE_ORDER")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrder {
	@Id
	@GeneratedValue (strategy =GenerationType.IDENTITY )
	private Long id;
	@NotBlank
	@JsonIgnore
	@ManyToOne (fetch = FetchType.LAZY,cascade = CascadeType.ALL,optional = false)
	@JoinColumn(name = "user_id")
	private User user;
	@NotBlank
	@JsonIgnore
	@ManyToOne (fetch = FetchType.LAZY,cascade = CascadeType.ALL,optional = false)
	@JoinColumn(name = "address_id")
	private Address address;
	@NotBlank
	
	@OneToMany (cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
	private List<Product> products= new ArrayList<>();
	@OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JsonIgnore
	private List<Quantity> quantities = new ArrayList<>();
}
