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

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Добавление нового пользователя
     *
     * @param user - Данные для нового пользователя (имя, тип, дата регистрации)
     * @return Сохранённый пользователь
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Получение пользователя с указанным идентификатором
     *
     * @param userId - Идентификатор пользователя
     */
    public User getUser(Long userId) throws DataNotFoundException {
        return userRepository.findById(userId).orElseThrow(DataNotFoundException::new);
    }

    /**
     * Добавление нового пользователя
     *
     * @param userId      - Идентификатор пользователя
     * @param updatedUser - Данные для нового пользователя (имя, тип, дата регистрации)
     * @return Обновленный пользователь
     */
    public User updateUser(User updatedUser, Long userId) throws DataNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("USER_NOT_FOUND"));
        user.setName(updatedUser.getName());
        user.setType(updatedUser.getType());
        user.setRegistrationDate(updatedUser.getRegistrationDate());
        return userRepository.save(user);
    }

    /**
     * Удаление существующего пользователя
     *
     * @param userId - Идентификатор пользователя, которого необходимо удалить
     */
    public void deleteUser(Long userId) {
        userRepository.delete(userRepository.getOne(userId));
    }

    /**
     * Получение всех пользователей
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
