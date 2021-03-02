package ftc.shift.sample.service;

import ftc.shift.sample.dto.UserDtoRequest;
import ftc.shift.sample.dto.UserDtoResponse;
import ftc.shift.sample.entity.User;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.mapper.UserMapper;
import ftc.shift.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Добавление нового пользователя
     *
     * @param user - Данные для нового пользователя (имя, тип, дата регистрации)
     * @return Сохранённый пользователь
     */
    public UserDtoResponse createUser(UserDtoRequest user) {
        return userMapper.userToDtoResponse(
            userRepository.save(userMapper.dtoRequestToUser(user)));
    }

    /**
     * Получение пользователя с указанным идентификатором
     *
     * @param userId - Идентификатор пользователя
     */
    public UserDtoResponse getUser(Integer userId) {
        return userMapper.userToDtoResponse(userRepository.getOne(userId));
    }

    /**
     * Добавление нового пользователя
     *
     * @param userId - Идентификатор пользователя
     * @param updatedUser - Данные для нового пользователя (имя, тип, дата регистрации)
     * @return Обновленный пользователь
     */
    public UserDtoResponse updateUser(UserDtoRequest updatedUser, Integer userId) {
        UserDtoResponse user = userMapper.userToDtoResponse(userRepository.findById(userId)
            .orElseThrow(() -> new DataNotFoundException("User not found")));
        user.setName(updatedUser.getName());
        user.setType(updatedUser.getType());
        user.setRegistrationDate(updatedUser.getRegistrationDate()); // вообще такое не надо изменять. Мб и не давать возможности?
        userRepository.save(userMapper.dtoResponseToUser(user));
        return user;
    }

    /**
     * Удаление существующего пользователя
     *
     * @param userId - Идентификатор пользователя, которого необходимо удалить
     */
    public void deleteUser(Integer userId) {
        userRepository.delete(userRepository.getOne(userId));
    }

    /**
     * Получение всех пользователей
     */
    public List<UserDtoResponse> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDtoResponse> userDtoResponceList = new ArrayList<UserDtoResponse>();
        userList.forEach(user -> {
            userDtoResponceList.add(userMapper.userToDtoResponse(user));
        });
        return userDtoResponceList;
    }
}
