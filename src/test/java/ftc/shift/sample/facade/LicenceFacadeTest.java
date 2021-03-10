package ftc.shift.sample.facade;

import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.entity.Customer;
import ftc.shift.sample.exception.BadRequestException;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.exception.LicenceException;
import ftc.shift.sample.exception.LicenceGeneratorException;
import ftc.shift.sample.service.LicenceService;
import ftc.shift.sample.service.CustomerService;
import ftc.shift.sample.util.Constants;
import ftc.shift.sample.util.LicenceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static ftc.shift.sample.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
class LicenceFacadeTest {

    @MockBean
    private LicenceService licenceService;

    @MockBean
    private CustomerService customerService;

    private LicenceFacade licenceFacade;

    @BeforeEach
    void setUp() {
        licenceFacade = new LicenceFacade(licenceService, customerService);
    }

    @Test
    void createLicence() throws LicenceGeneratorException {
        licenceFacade.createLicence(1L);
        verify(licenceService, times(1)).createLicence(any(Licence.class));
    }

    @Test
    void getAllUserLicencesId() throws DataNotFoundException {
        Long id = 1L;

        Customer customer = new Customer();
        customer.setId(id);
        customer.setType(Constants.CUSTOMER_TYPE_USER);

        when(customerService.getCustomer(id)).thenReturn(customer);

        Exception exception = assertThrows(BadRequestException.class, () ->
                licenceFacade.getAllCompanyLicencesId(id));
        assertEquals(USER_IS_NOT_COMPANY, exception.getMessage());
    }

    @Test
    void getAllCompanyLicencesId() throws BadRequestException, DataNotFoundException {
        Long id = 1L;

        Customer customer = new Customer();
        customer.setId(id);
        customer.setType(Constants.CUSTOMER_TYPE_COMPANY);

        when(customerService.getCustomer(id)).thenReturn(customer);

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
    void isLicenceCorrectIfNotExist() {
        Exception exception = assertThrows(LicenceException.class, () ->
                licenceFacade.isLicenceCorrect("badLicenceString"));
        assertEquals(LICENCE_NOT_EXIST, exception.getMessage());
    }

    @Test
    void isLicenceCorrectIfExpired() throws LicenceGeneratorException, DataNotFoundException, LicenceException {
        Licence licenceExpired = LicenceUtil.generateLicence(1L, 0L);
        when(licenceService.getLicence(licenceExpired.getId())).thenReturn(licenceExpired);
        Exception exception = assertThrows(LicenceException.class, () ->
                licenceFacade.isLicenceCorrect(LicenceUtil.generateLicenseString(licenceExpired)));
        assertEquals(LICENCE_EXPIRED, exception.getMessage());

        Licence licence = LicenceUtil.generateLicence(1L, 100L);
        when(licenceService.getLicence(licence.getId())).thenReturn(licence);
        assertTrue(licenceFacade.isLicenceCorrect(LicenceUtil.generateLicenseString(licence)));
    }

    @Test
    void isLicenceCorrect() throws LicenceGeneratorException, DataNotFoundException, LicenceException {
        Licence licence = LicenceUtil.generateLicence(1L, 100L);
        when(licenceService.getLicence(licence.getId())).thenReturn(licence);
        assertTrue(licenceFacade.isLicenceCorrect(LicenceUtil.generateLicenseString(licence)));
    }
}