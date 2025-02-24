package project.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import project.rest.common.NotFoundException;
import project.rest.model.AddressDTO;
import project.rest.model.CompanyDTO;
import project.rest.model.GeoDTO;
import project.rest.model.UserDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Test
    public void whenGetUsers_ThenReturnUsers() {
        List<UserDTO> userDTOs = userService.getUsers();
        assertNotNull(userDTOs);
    }

    @Test
    public void whenGetUserById_ThenReturnUser() {
        UserDTO userDTO = userService.getUserById(1);
        assertNotNull(userDTO);
        assertEquals(1, userDTO.getId());
    }

    @Test
    public void whenGetUserById_ThenReturnNotFoundException(){
        assertThrows(NotFoundException.class, () -> userService.getUserById(2));
    }

    @Test
    public void whenCreateUser_ThenReturnUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(111);
        userDTO.setName("User 3");
        userDTO.setWebsite("www.user3.com");
        userDTO.setPhone("123456789");

        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Company 1");
        companyDTO.setCatchPhrase("www.company1.com");
        companyDTO.setBs("987654321");
        userDTO.setCompany(companyDTO);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("Street 1");
        addressDTO.setSuite("Suite 1");
        addressDTO.setCity("City 1");
        addressDTO.setZipcode("12345");

        GeoDTO geoDTO = new GeoDTO();
        geoDTO.setLat("-37.3159");
        geoDTO.setLng("81.1496");

        addressDTO.setGeo(geoDTO);


        UserDTO createdUser = userService.updateUser(userDTO.getId(), userDTO);

        assertNotNull(createdUser);
        assertEquals(111, createdUser.getId());
    }

}
