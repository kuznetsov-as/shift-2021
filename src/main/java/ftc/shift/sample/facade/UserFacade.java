package ftc.shift.sample.facade;

import ftc.shift.sample.dto.UserDtoRequest;
import ftc.shift.sample.dto.UserDtoResponse;
import ftc.shift.sample.entity.User;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.mapper.UserMapper;
import ftc.shift.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserFacade {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserFacade(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public UserDtoResponse createUser(UserDtoRequest user) {
        User creatingUser = userMapper.dtoRequestToUser(user);
        User createdUser =userService.createUser(creatingUser);
        return userMapper.userToDtoResponse(createdUser);
    }

    public UserDtoResponse getUser(Long userId) throws DataNotFoundException {
        return userMapper.userToDtoResponse(userService.getUser(userId));
    }

    public UserDtoResponse updateUser(UserDtoRequest user, Long userId) throws DataNotFoundException {
        User updatingUser = userMapper.dtoRequestToUser(user);
        User updatedUser = userService.updateUser(updatingUser, userId);
        return userMapper.userToDtoResponse(updatedUser);
    }

    public void deleteUser(Long userId) {
        userService.deleteUser(userId);
    }

    public List<UserDtoResponse> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        List<UserDtoResponse> userDtoResponseList = new ArrayList<>();
        userList.forEach(user -> userDtoResponseList.add(userMapper.userToDtoResponse(user)));
        return userDtoResponseList;
    }
}
