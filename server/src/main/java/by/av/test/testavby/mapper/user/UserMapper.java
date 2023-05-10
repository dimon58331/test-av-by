package by.av.test.testavby.mapper.user;

import by.av.test.testavby.dto.UserDTO;
import by.av.test.testavby.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User convertUserDTOToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO convertUserToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
