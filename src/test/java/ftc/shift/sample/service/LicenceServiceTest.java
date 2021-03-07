package ftc.shift.sample.service;

import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.exception.LicenceGeneratorException;
import ftc.shift.sample.repository.LicenceRepository;
import ftc.shift.sample.util.LicenceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class LicenceServiceTest {

    @MockBean
    private LicenceRepository repository;

    private LicenceService licenceService;

    @BeforeEach
    void setUp() {
        licenceService = new LicenceService(repository);
    }

    @Test
    void createLicence() {
        licenceService.createLicence(new Licence());
        verify(repository, times(1)).save(any(Licence.class));
    }

    @Test
    void getLicenceCompany() {
        List<Licence> licenceList = new LinkedList<>();

        Licence notLastLicence = new Licence();
        licenceList.add(notLastLicence);

        Licence lastLicence = new Licence();
        licenceList.add(lastLicence);

        when(repository.findLicencesByUserId(1L)).thenReturn(licenceList);
        Licence result = licenceService.getLicenceCompany(1L);
        assertEquals(lastLicence, result);
        assertNotEquals(notLastLicence, result);
    }

    @Test
    void getLicence() throws DataNotFoundException {
        Exception exception = assertThrows(DataNotFoundException.class, () ->
                licenceService.getLicence(null));
        assertEquals("LICENSE_NOT_EXIST", exception.getMessage());

        UUID uuid = UUID.randomUUID();
        Licence licence = new Licence();
        when(repository.findById(uuid)).thenReturn(java.util.Optional.of(licence));

        Licence result = licenceService.getLicence(uuid);
        assertEquals(licence, result);
    }

    @Test
    void getAllCompanyLicencesId() throws LicenceGeneratorException {
        List<Licence> licences = new LinkedList<>();

        Licence firstLicence = LicenceUtil.generateLicence(1L, 100L);
        Licence secondLicence = LicenceUtil.generateLicence(1L, 100L);

        licences.add(firstLicence);
        licences.add(secondLicence);

        when(repository.findLicencesByUserId(1L)).thenReturn(licences);
        List<UUID> licenceIdList = licenceService.getAllCompanyLicencesId(1L);
        assertEquals(firstLicence.getId(), licenceIdList.get(0));
        assertEquals(secondLicence.getId(), licenceIdList.get(1));
    }
}