package ftc.shift.sample.controller;

import ftc.shift.sample.dto.UserDtoRequest;
import ftc.shift.sample.dto.UserDtoResponse;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsersController {
    private static final String USERS_PATH = "/users";
    private final UserService service;

    @Autowired
    public UsersController(UserService service) {
        this.service = service;
    }

    /**
     * Добавление нового пользователя
     *
     * @param user - Данные для нового пользователя (имя, тип, дата регистрации)
     * @return Сохранённый пользователь
     */
    @PostMapping(USERS_PATH)
    public ResponseEntity<UserDtoResponse> createUser(@RequestBody UserDtoRequest user) {
        UserDtoResponse result = service.createUser(user);
        return ResponseEntity.ok(result);
    }

    /**
     * Получение пользователя с указанным идентификатором
     *
     * @param userId - Идентификатор пользователя
     */
    @GetMapping(USERS_PATH + "/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(service.getUser(userId));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Обновление существующего пользователя
     *
     * @param userId - Идентификатор пользователя
     * @param user   - Новые данные для пользователя (имя, тип, дата регистрации)
     * @return Обновленный пользователь
     */
    @PostMapping(USERS_PATH + "/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody UserDtoRequest user, @PathVariable Long userId) {

        try {
            UserDtoResponse updatedUser = service.updateUser(user, userId);
            return ResponseEntity.ok(updatedUser);
        } catch (DataNotFoundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    /**
     * Удаление существующего пользователя
     *
     * @param userId - Идентификатор пользователя, которого необходимо удалить
     */
    @DeleteMapping(USERS_PATH + "/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        service.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Получение всех пользователей
     */
    @GetMapping(USERS_PATH)
    public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
        List<UserDtoResponse> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }
}