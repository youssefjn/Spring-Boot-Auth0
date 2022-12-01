package spring.boot.yj.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "ADDRESS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id ;
	@NotBlank
	@Column (length = 255)
	private String city;
	@NotBlank
	@Column (length = 255)
	private String country;
	@NotBlank
	@Column (length = 255,name = "postcode")
	private String postCode;
	@NotBlank
	@Column (length = 255,name = "address_line_1")
	private String adLine1;
	@NotBlank
	@Column (length = 255,name = "address_line_2")
	private String adLine2;
	@JsonIgnore
	@ManyToOne(optional = false )
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
}
