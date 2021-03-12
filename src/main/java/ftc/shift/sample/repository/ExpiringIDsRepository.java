package ftc.shift.sample.repository;

import ftc.shift.sample.entity.ExpiringIDs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ExpiringIDsRepository extends JpaRepository<ExpiringIDs, Long> {
    @Modifying
    @Query(value = "call licence_to_expire()", nativeQuery = true)
    void update();
}