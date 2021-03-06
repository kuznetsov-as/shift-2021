package ftc.shift.sample.service;

import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.exception.LicenceDecodeException;
import ftc.shift.sample.exception.LicenceException;
import ftc.shift.sample.repository.LicenceRepository;
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

    @Autowired
    public LicenceService(LicenceRepository licenceRepository) {
        this.licenceRepository = licenceRepository;
    }

    public Licence createLicence(Licence licence) {
        return licenceRepository.save(licence);
    }

    public Licence getLicenceCompany(Long companyId) {
        List<Licence> licenceList = licenceRepository.findLicencesByUserId(companyId);
        return licenceList.get(licenceList.size() - 1);
    }

    public Licence getLicence(UUID licenceId) throws DataNotFoundException {
        return licenceRepository.findById(licenceId).orElseThrow(() -> new DataNotFoundException("LICENSE_NOT_EXIST"));
    }

    public List<UUID> getAllCompanyLicencesId(Long id) {
        List<Licence> licenceList = licenceRepository.findLicencesByUserId(id);
        List<UUID> licenceIdList = new LinkedList<>();
        for (Licence licence : licenceList) {
            licenceIdList.add(licence.getId());
        }
        return licenceIdList;
    }
}
