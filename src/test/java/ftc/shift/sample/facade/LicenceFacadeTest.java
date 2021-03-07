package ftc.shift.sample.facade;

import ftc.shift.sample.dto.UserDtoResponse;
import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.exception.*;
import ftc.shift.sample.service.LicenceService;
import ftc.shift.sample.service.UserService;
import ftc.shift.sample.util.Constants;
import ftc.shift.sample.util.LicenceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class LicenceFacadeTest {

    @MockBean
    private LicenceService licenceService;

    @MockBean
    private UserService userService;

    private LicenceFacade licenceFacade;

    @BeforeEach
    void setUp() {
        licenceFacade = new LicenceFacade(licenceService, userService);
    }

    @Test
    void createLicence() throws LicenceGeneratorException {
        licenceFacade.createLicence(1L);
        verify(licenceService, times(1)).createLicence(any(Licence.class));
    }

    @Test
    void getAllCompanyLicencesId() throws BadRequestException, DataNotFoundException {
        Long id = 1L;

        UserDtoResponse userDtoResponse = new UserDtoResponse();
        userDtoResponse.setId(id);
        userDtoResponse.setType(Constants.USER_TYPE_USER);

        when(userService.getUser(id)).thenReturn(userDtoResponse);

        Exception exception = assertThrows(BadRequestException.class, () ->
                licenceFacade.getAllCompanyLicencesId(id));
        assertEquals("USER_IS_NOT_COMPANY", exception.getMessage());

        userDtoResponse.setType(Constants.USER_TYPE_COMPANY);
        when(userService.getUser(id)).thenReturn(userDtoResponse);

        licenceFacade.getAllCompanyLicencesId(id);
        verify(licenceService, times(1)).getAllCompanyLicencesId(id);
    }

    @Test
    void getLicence() throws DataNotFoundException {
        UUID uuid = UUID.randomUUID();
        licenceFacade.getLicence(uuid);
        verify(licenceService, times(1)).getLicence(uuid);


    }

    @Test
    void isLicenceCorrect() throws LicenceGeneratorException, DataNotFoundException, LicenceException {
        Exception exception = assertThrows(LicenceException.class, () ->
                licenceFacade.isLicenceCorrect("licenceString"));
        assertEquals("LICENSE_NOT_EXIST", exception.getMessage());

        Licence licenceExpired = LicenceUtil.generateLicence(1L, 0L);
        when(licenceService.getLicence(licenceExpired.getId())).thenReturn(licenceExpired);
        exception = assertThrows(LicenceException.class, () ->
                licenceFacade.isLicenceCorrect(LicenceUtil.generateLicenseString(licenceExpired)));
        assertEquals("LICENSE_EXPIRED", exception.getMessage());

        Licence licence = LicenceUtil.generateLicence(1L, 100L);
        when(licenceService.getLicence(licence.getId())).thenReturn(licence);
        assertTrue(licenceFacade.isLicenceCorrect(LicenceUtil.generateLicenseString(licence)));

    }
}