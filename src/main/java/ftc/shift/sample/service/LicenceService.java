package ftc.shift.sample.service;

import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.entity.User;
import ftc.shift.sample.exception.*;
import ftc.shift.sample.repository.LicenceRepository;
import ftc.shift.sample.repository.UserRepository;
import ftc.shift.sample.util.LicenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class LicenceService {

    private final LicenceRepository licenceRepository;
    private final UserRepository userRepository;

    @Autowired
    public LicenceService(LicenceRepository licenceRepository, UserRepository userRepository) {
        this.licenceRepository = licenceRepository;
        this.userRepository = userRepository;
    }

    public String createLicence(Long userId, Long duration) throws LicenceGeneratorException {
        Licence licence = LicenceUtil.generateLicence(userId, duration);
        return LicenceUtil.generateLicenseString(licenceRepository.save(licence));
    }

    public String createLicence(Long userId) throws LicenceGeneratorException {
        Licence licence = LicenceUtil.generateLicence(userId, 100L);
        return LicenceUtil.generateLicenseString(licenceRepository.save(licence));
    }

    public String getLicence(UUID licenceId, Long userId) throws DataNotFoundException {

        Licence licence = licenceRepository.findById(licenceId).orElseThrow(() -> new DataNotFoundException("LICENSE_NOT_EXIST"));

        if (licence.getUserId().equals(userId)) {
            if (licence.getType().equals("company")) {
                List<Licence> licenceList = licenceRepository.findLicencesByUserId(userId);
                return LicenceUtil.generateLicenseString(licenceList.get(licenceList.size() - 1));
            }
            return getLicenseById(licenceId);
        } else {
            throw new DataNotFoundException("LICENSE_NOT_EXIST");
        }
    }

    public List<UUID> getAllCompanyLicencesId(Long id) throws DataNotFoundException, BadRequestException {

        User user = userRepository.findById(id).orElseThrow(DataNotFoundException::new);

        if (user.getType().equals("company")) {
            List<Licence> licenceList = licenceRepository.findLicencesByUserId(id);
            List<UUID> licenceIdList = new LinkedList<>();
            for (Licence licence : licenceList) {
                licenceIdList.add(licence.getId());
            }
            return licenceIdList;
        } else {
            System.out.println("Is not company");
            throw new BadRequestException("USER_IS_NOT_COMPANY");
        }
    }

    public boolean isLicenceCorrect(String licenceString) throws LicenceException, DataNotFoundException {
        try {
            LicenceUtil.PublicLicence licence = LicenceUtil.getLicenseFromString(licenceString);

            Licence privateLicence = licenceRepository.findById(licence.getId()).orElseThrow(DataNotFoundException::new);

            if (Date.valueOf(LocalDate.now()).after(privateLicence.getEndDate())) {

                String privateKey = licenceRepository.getOne(licence.getId()).getPrivateKey();

                return LicenceUtil.isLicenceCorrect(licence, privateKey);

            } else {
                throw new LicenceException("LICENSE_EXPIRED");
            }
        } catch (LicenceDecodeException e) {
            throw new LicenceException("LICENSE_NOT_EXIST");
        }
    }

    public String getLicenseById(UUID uuid) throws DataNotFoundException {
        var licence = licenceRepository.findById(uuid)
                .orElseThrow(()->new DataNotFoundException("LICENSE_NOT_EXIST"));

        return LicenceUtil.generateLicenseString(licence);
    }

}
