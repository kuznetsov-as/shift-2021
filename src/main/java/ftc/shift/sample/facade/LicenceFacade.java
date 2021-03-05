package ftc.shift.sample.facade;

import ftc.shift.sample.dto.UserDtoResponse;
import ftc.shift.sample.util.Constants;
import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.exception.*;
import ftc.shift.sample.service.LicenceService;
import ftc.shift.sample.service.UserService;
import ftc.shift.sample.util.LicenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class LicenceFacade {

    private final LicenceService licenceService;
    private final UserService userService;

    @Autowired
    public LicenceFacade(LicenceService licenceService, UserService userService) {
        this.licenceService = licenceService;
        this.userService = userService;
    }

    // Почему два метода, первый где-то понадобится?
    // Может быть поставим ограничение, что мы не будем создавать лицензию, если пользователь не выбрал срок?

    public String createLicence(Long userId, Long duration) throws LicenceGeneratorException {
        Licence licence = LicenceUtil.generateLicence(userId, duration);
        return LicenceUtil.generateLicenseString(licenceService.createLicence(licence));
    }

    public String createLicence(Long userId) throws LicenceGeneratorException {
        Licence licence = LicenceUtil.generateLicence(userId, 100L);
        return LicenceUtil.generateLicenseString(licenceService.createLicence(licence));
    }

    public List<UUID> getAllCompanyLicencesId(Long id) throws DataNotFoundException, BadRequestException {
        UserDtoResponse user = userService.getUser(id);

        if (Constants.USER_TYPE_COMPANY.equals(user.getType())) {
            return licenceService.getAllCompanyLicencesId(id);
        } else {
            System.out.println("Is not company");
            throw new BadRequestException("USER_IS_NOT_COMPANY");
        }
    }

    public String getLicence(UUID licenceId, Long userId) throws DataNotFoundException {
        Licence licence = licenceService.getLicence(licenceId);
        UserDtoResponse user = userService.getUser(userId);

        if (licence.getUserId().equals(userId)) {
            if (Constants.USER_TYPE_COMPANY.equals(user.getType())) {
                licence = licenceService.getLicenceCompany(userId);
                return LicenceUtil.generateLicenseString(licence);
            }
            return getLicence(licenceId);
        } else {
            throw new DataNotFoundException("LICENSE_NOT_EXIST");
        }
    }

    public String getLicence(UUID licenceId) throws DataNotFoundException {
        return LicenceUtil.generateLicenseString(licenceService.getLicence(licenceId));
    }

    public boolean isLicenceCorrect(String licenceString) throws LicenceException, DataNotFoundException {
        try {
            LicenceUtil.PublicLicence licence = LicenceUtil.getLicenseFromString(licenceString);
            Licence privateLicence = licenceService.getLicence(licence.getId());

            if (Date.valueOf(LocalDate.now()).after(privateLicence.getEndDate())) {
                String privateKey = privateLicence.getPrivateKey();
                return LicenceUtil.isLicenceCorrect(licence, privateKey);
            } else {
                throw new LicenceException("LICENSE_EXPIRED");
            }
        } catch (LicenceDecodeException e) {
            throw new LicenceException("LICENSE_NOT_EXIST");
        }
    }
}
