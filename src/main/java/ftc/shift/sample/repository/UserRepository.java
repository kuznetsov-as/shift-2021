package ftc.shift.sample.repository;

import ftc.shift.sample.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для получения данных по пользователям
 */

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
