package project.rest.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDTO{
	private String street;
	private String suite;
	@NotBlank
	private String city;
	@NotBlank
	private String zipcode;
	private GeoDTO geo;
}