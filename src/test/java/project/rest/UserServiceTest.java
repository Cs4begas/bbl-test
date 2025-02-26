package project.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import project.rest.common.NotFoundException;
import project.rest.model.UserDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    private final String userMockFile = "user_data.json";


    public <T> T readJsonFileToObject(String filePath, Class<T> objectType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource(filePath);
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, objectType);
        }
    }


    @Test
    public void whenGetUsers_ThenReturnUsers() {
        List<UserDTO> userDTOs = userService.getUsers();
        assertNotNull(userDTOs);
    }

    @Test
    public void givenUserId_whenGetUserById_ThenReturnUser() {
        UserDTO userDTO = userService.getUserById(1);
        assertNotNull(userDTO);
        assertEquals(1, userDTO.getId());
    }

    @Test
    public void givenNoUserId_whenGetUserById_ThenReturnNotFoundException(){
        assertThrows(NotFoundException.class, () -> userService.getUserById(555));
    }

    @Test
    public void givenUserRequest_whenCreateUser_ThenReturnUser() throws IOException {

        UserDTO userDTO = readJsonFileToObject(userMockFile, UserDTO.class);

        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(userDTO);
        assertEquals("Test Meow", createdUser.getName());
        assertEquals(11, userService.getUsers().size());
    }

    @Test
    public void whenUpdateUser_ThenSuccessUpdateUser() throws IOException {
        UserDTO userDTO = readJsonFileToObject(userMockFile, UserDTO.class);

        userDTO.setName("PPPP");

        UserDTO updateUser = userService.updateUser(2, userDTO);

        assertNotNull(updateUser);
        assertEquals(1, updateUser.getId());
        assertEquals("PPPP", updateUser.getName());
        assertNotEquals("Leanne Graham", updateUser.getName());
    }

    @Test
    public void whenDeleteUser_ThenSuccessDeleteUser() {
        userService.deleteUser(1);
        assertEquals(9, userService.getUsers().size());
    }


}
