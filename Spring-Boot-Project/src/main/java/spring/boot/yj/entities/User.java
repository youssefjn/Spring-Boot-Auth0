package spring.boot.yj.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table ( name = "USER")
@Data
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(nullable = false,unique = true, length = 255)
	private String username;
	
	@Column(name = "firstname", length = 255)
	@NotBlank
	private String firstName;
	
	@Column(name = "lastname", length = 255)
	@NotBlank
	private String lastName;
	@JsonIgnore
	@NotBlank
	@Column(nullable = false, length = 255)
	private String password;
	
	@Email
	@NotBlank
	@Column ( unique = true , nullable = false , length = 255)
	private String email;
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true , fetch = FetchType.EAGER)
	private List<Address> addresses = new ArrayList<>();
}
