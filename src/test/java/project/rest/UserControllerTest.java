package project.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import project.rest.common.NotFoundException;
import project.rest.model.UserDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws IOException{
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    public <T> T readJsonFileToObject(String filePath, Class<T> objectType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource(filePath);
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, objectType);
        }
    }

    @Test
    public void givenNoUsers_whenGetUsers_thenReturnEmptyJsonArray() throws Exception {
        when(userService.getUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void givenValidUserId_whenGetUserById_thenReturnJson() throws Exception {
        int userId = 1;
        UserDTO user = new UserDTO(/* ... initialize with sample data ... */);
        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/users/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    public void givenInvalidUserId_whenGetUserById_thenReturnNotFound() throws Exception {
        int userId = 999;
        when(userService.getUserById(userId)).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/users/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateUser_thenReturnCreatedUser() throws Exception {
        UserDTO userDTO = readJsonFileToObject("user_data.json", UserDTO.class);
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonList() throws Exception {
        UserDTO userDTO = readJsonFileToObject("user_data.json", UserDTO.class);

        List<UserDTO> userDTOs = List.of(userDTO);
        when(userService.getUsers()).thenReturn(userDTOs);

        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$..id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Test Meow"));

    }


    @Test
    public void whenUpdateUser_thenReturnUpdatedUser() throws Exception {
        int userId = 1;
        UserDTO user = new UserDTO();
        user.setUsername("PPPPP");
        when(userService.updateUser(eq(userId), any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()));
    }



    @Test
    public void whenDeleteUser_thenReturnOk() throws Exception {
        int userId = 1;

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());
    }

}
