package project.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	private int id;
	@NotBlank
	private String name;
	@NotBlank
	private String email;
	private String username;
	@NotBlank
	private String phone;
	private String website;
	@Valid
	private CompanyDTO company;
	@Valid
	private AddressDTO address;


}