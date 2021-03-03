package ftc.shift.sample.service;

import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.exception.LicenceDecodeException;
import ftc.shift.sample.exception.LicenceGeneratorException;
import ftc.shift.sample.repository.LicenceRepository;
import ftc.shift.sample.utils.LicenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LicenseService {

    private final LicenceRepository licenseRepository;

    @Autowired
    public LicenseService(LicenceRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    public String createLicence(Long userId, Long duration) throws LicenceGeneratorException {
        Licence licence = LicenceUtil.generateLicence(userId, duration);
        return LicenceUtil.generateLicenseString(licenseRepository.save(licence));
    }

    public boolean isLicenceCorrect(String licenceString) {

        try {
            LicenceUtil.PublicLicence licence = LicenceUtil.getLicenseFromString(licenceString);

            String privateKey = licenseRepository.getOne(licence.getId()).getPrivateKey();

            return LicenceUtil.isLicenceCorrect(licence, privateKey);


        } catch (LicenceDecodeException e) {
            return false;
        }
    }

    public String getLicenseById(UUID uuid){
        var licence = licenseRepository.getOne(uuid);
        return LicenceUtil.generateLicenseString(licence);
    }

}
