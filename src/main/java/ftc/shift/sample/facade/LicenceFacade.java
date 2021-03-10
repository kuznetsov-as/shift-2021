package ftc.shift.sample.facade;

import ftc.shift.sample.entity.Customer;
import ftc.shift.sample.util.Constants;
import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.exception.*;
import ftc.shift.sample.service.LicenceService;
import ftc.shift.sample.service.CustomerService;
import ftc.shift.sample.util.LicenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static ftc.shift.sample.util.Constants.LICENCE_NOT_EXIST;
import static ftc.shift.sample.util.Constants.USER_IS_NOT_COMPANY;

@Service
public class LicenceFacade {

    private final LicenceService licenceService;
    private final CustomerService customerService;

    @Autowired
    public LicenceFacade(LicenceService licenceService, CustomerService customerService) {
        this.licenceService = licenceService;
        this.customerService = customerService;
    }

    public String createLicence(Long userId) throws LicenceGeneratorException {
        Licence licence = LicenceUtil.generateLicence(userId, 100L);
        return LicenceUtil.generateLicenseString(licenceService.createLicence(licence));
    }

    public List<UUID> getAllCompanyLicencesId(Long id) throws DataNotFoundException, BadRequestException {
        Customer customer = customerService.getCustomer(id);

        if (Constants.CUSTOMER_TYPE_COMPANY.equals(customer.getType())) {
            return licenceService.getAllCompanyLicencesId(id);
        } else {
            throw new BadRequestException(USER_IS_NOT_COMPANY);
        }
    }

    public String getLicence(UUID licenceId, Long userId) throws DataNotFoundException {
        Licence licence = licenceService.getLicence(licenceId);
        Customer customer = customerService.getCustomer(userId);

        if (licence.getCustomer().getId().equals(userId)) {
            if (Constants.CUSTOMER_TYPE_COMPANY.equals(customer.getType())) {
                licence = licenceService.getLicenceCompany(userId);
                return LicenceUtil.generateLicenseString(licence);
            }
            return getLicence(licenceId);
        } else {
            throw new DataNotFoundException(LICENCE_NOT_EXIST);
        }
    }

    public String getLicence(UUID licenceId) throws DataNotFoundException {
        return LicenceUtil.generateLicenseString(licenceService.getLicence(licenceId));
    }

    public boolean isLicenceCorrect(String licenceString) throws LicenceException, DataNotFoundException {
        try {
            LicenceUtil.PublicLicence licence = LicenceUtil.getLicenseFromString(licenceString);
            Licence privateLicence = licenceService.getLicence(licence.getId());

            if (Date.valueOf(LocalDate.now()).before(privateLicence.getEndDate())) {
                String privateKey = privateLicence.getPrivateKey();
                return LicenceUtil.isLicenceCorrect(licence, privateKey);
            } else {
                throw new LicenceException("LICENCE_EXPIRED");
            }
        } catch (LicenceDecodeException e) {
            throw new LicenceException(LICENCE_NOT_EXIST);
        }
    }
}
