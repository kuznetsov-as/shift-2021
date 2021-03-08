package ftc.shift.sample.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Родительский класс для всех энтити (у которых простые id)
 */

@MappedSuperclass
@Getter
@Setter
@Accessors(chain = true)
public abstract class Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
}

