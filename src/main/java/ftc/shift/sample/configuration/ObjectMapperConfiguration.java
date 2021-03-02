package ftc.shift.sample.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import javax.annotation.ParametersAreNonnullByDefault;
import java.text.SimpleDateFormat;

@Configuration
//@ParametersAreNonnullByDefault
public class ObjectMapperConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

}
