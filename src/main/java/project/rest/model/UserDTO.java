package project.rest.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
public class UserDTO {
	private int id;
	private String name;
	private String email;
	private String username;
	private String phone;
	private String website;
	private CompanyDTO company;
	private AddressDTO address;


}