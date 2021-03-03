package ftc.shift.sample.entity;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "licenses")
public class Licence {

    @Id

    /*
     * Сериализатору нужно обязательно говорить учитывать аннотации
     * Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
     */

    @Expose//Анотация говорит gson сериализовать это поле
    private UUID id;

    private String privateKey;

    @Expose
    private String licenseKey;

    @Expose
    private Date createDate;

    @Expose
    private Date endDate;

    private Long userId;

    @Expose
    private String type;
}
