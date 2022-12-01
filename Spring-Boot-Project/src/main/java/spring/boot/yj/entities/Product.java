package spring.boot.yj.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "PRODUCT")
@Getter
@Setter
@AllArgsConstructor
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
	@JsonIgnore
	@OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE, optional = false, orphanRemoval = true)
	private Inventory inventory;

}
