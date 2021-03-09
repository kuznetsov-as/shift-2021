package ftc.shift.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ftc.shift.sample.facade.CustomerFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LicencesController.class)
class CustomersControllerTest {

    private static final String USERS_PATH = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerFacade customerFacade;

    @Test
    void createUser() {

    }
}