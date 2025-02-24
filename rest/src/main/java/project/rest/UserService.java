package project.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import project.rest.common.NotFoundException;
import project.rest.model.UserDTO;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {


    @Getter
    private List<UserDTO> users;

    public UserService() throws IOException {
        users = new ObjectMapper().readValue(new ClassPathResource("users_data.json").getInputStream(), new TypeReference<List<UserDTO>>() {
        });
    }


    public List<UserDTO> getUsers() {
        return users;
    }

    public UserDTO getUserById(Integer id) {
        return users.stream().filter(user -> user.getId() == id).findFirst().orElseThrow(
                () -> new NotFoundException("User not found with id: " + id)
        );
    }


    public UserDTO createUser(UserDTO userDTO) {
        users.add(userDTO);
        return userDTO;
    }

    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        UserDTO user = getUserById(id);
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setWebsite(userDTO.getWebsite());
        if(userDTO.getAddress() != null) {
            user.setAddress(userDTO.getAddress());
        }
        if(userDTO.getCompany() != null) {
            user.setCompany(userDTO.getCompany());
        }
        return userDTO;
    }

    public void deleteUser(Integer id) {
        UserDTO user = getUserById(id);
        users.remove(user);
    }
}
