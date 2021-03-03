package ftc.shift.sample.service;

import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.exception.*;
import ftc.shift.sample.repository.LicenceRepository;
import ftc.shift.sample.repository.UserRepository;
import ftc.shift.sample.utils.LicenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class LicenseService {

    private final LicenceRepository licenseRepository;
    private final UserRepository userRepository;

    @Autowired
    public LicenseService(LicenceRepository licenseRepository, UserRepository userRepository) {
        this.licenseRepository = licenseRepository;
        this.userRepository = userRepository;
    }

    public String createLicence(Long userId, Long duration) throws LicenceGeneratorException {
        Licence licence = LicenceUtil.generateLicence(userId, duration);
        return LicenceUtil.generateLicenseString(licenseRepository.save(licence));
    }

    public String createLicence(Long userId) throws LicenceGeneratorException {
        Licence licence = LicenceUtil.generateLicence(userId, 100L);
        return LicenceUtil.generateLicenseString(licenseRepository.save(licence));
    }

    public String getLicence(UUID licenceId, Long userId) {
        if (licenseRepository.getOne(licenceId).getUserId().equals(userId)) {
            if (userRepository.getOne(userId).getType().equals("company")) {
                List<Licence> licenceList = licenseRepository.findLicencesByUserId(userId);
                return LicenceUtil.generateLicenseString(licenceList.get(licenceList.size() - 1));
            }
            return getLicenseById(licenceId);
        } else {
            throw new DataNotFoundException("Лицензия не найдена");
        }
    }

    public List<UUID> getAllCompanyLicencesId(Long id) {
        if (userRepository.getOne(id).getType().equals("company")) {
            List<Licence> licenceList = licenseRepository.findLicencesByUserId(id);
            List<UUID> licenceIdList = new LinkedList<>();
            for (Licence licence : licenceList) {
                licenceIdList.add(licence.getId());
            }
            return licenceIdList;
        } else {
            throw new BadRequestException("Запрос возможен только для компаний");
        }
    }

    public boolean isLicenceCorrect(String licenceString) throws LicenceCorrectnessException {
        try {
            LicenceUtil.PublicLicence licence = LicenceUtil.getLicenseFromString(licenceString);

            if (licenseRepository.getOne(licence.getId()).getEndDate().before(new Date())) {
                String privateKey = licenseRepository.getOne(licence.getId()).getPrivateKey();
                return LicenceUtil.isLicenceCorrect(licence, privateKey);
            } else {
                throw new LicenceCorrectnessException("LICENSE_EXPIRED");
            }
        } catch (LicenceDecodeException e) {
            throw new LicenceCorrectnessException("LICENSE_NOT_EXIST");
        }
    }

    public String getLicenseById(UUID uuid) {
        var licence = licenseRepository.getOne(uuid);
        return LicenceUtil.generateLicenseString(licence);
    }

}
