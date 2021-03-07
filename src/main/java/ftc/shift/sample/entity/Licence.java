package ftc.shift.sample.entity;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "licences")
public class Licence {


    /*
     * Сериализатору нужно обязательно говорить учитывать аннотации
     * Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
     */

    @Id
    @Expose//Анотация говорит gson сериализовать это поле
    private UUID id;

    @NonNull
    private String privateKey;

    @NonNull
    @Expose
    private String licenceKey;

    @NonNull
    @Expose
    private Date createDate;

    @NonNull
    @Expose
    private Date endDate;

    @NonNull
    private Long userId;

    @NonNull
    @Expose
    private String type;

    @Nullable
    @Expose
    private Integer numberOfLicences;
}
