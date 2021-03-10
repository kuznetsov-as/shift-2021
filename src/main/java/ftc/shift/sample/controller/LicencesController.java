package ftc.shift.sample.controller;

import ftc.shift.sample.exception.BadRequestException;
import ftc.shift.sample.exception.DataNotFoundException;
import ftc.shift.sample.exception.LicenceException;
import ftc.shift.sample.exception.LicenceGeneratorException;
import ftc.shift.sample.facade.LicenceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static ftc.shift.sample.util.Constants.LICENCE_GENERATION_ERROR;
import static ftc.shift.sample.util.Constants.LICENCE_NOT_EXIST;

@RestController
public class LicencesController {
    private static final String LICENCES_PATH = "/licences";
    private final LicenceFacade licenceFacade;

    @Autowired
    public LicencesController(LicenceFacade licenceFacade) {
        this.licenceFacade = licenceFacade;
    }

    /**
     * Добавление новой лицензии
     *
     * @param id - id пользователя или компании, для новой лицензии
     * @return Сохранённую лицензию
     */
    @PostMapping(LICENCES_PATH + "/new")
    public ResponseEntity<String> createLicence(@RequestBody Long id) {
        try {
            String result = licenceFacade.createLicence(id);
            return ResponseEntity.ok(result);
        } catch (LicenceGeneratorException exception) {
            return ResponseEntity.status(418).body("LICENCE_GENERATION_ERROR");
        }
    }

    /**
     * Получение существующей лицензии
     *
     * @param id        - id пользователя или компании
     * @param licenceId - id лицензии
     * @return Уже выданную лицензию, если userId для данной лицензии совпадает.
     * Если id компании, то возвращает последнюю выданную лицензию.
     */
    @PostMapping(LICENCES_PATH + "/{licenceId}")
    public ResponseEntity<String> getLicence(@PathVariable UUID licenceId, @RequestBody Long id) {
        try {
            String result = licenceFacade.getLicence(licenceId, id);
            return ResponseEntity.ok(result);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Получение всех лицензий компании
     *
     * @param id - id компании
     * @return Список всех ID лицензий для полученного id компании
     */
    @PostMapping(LICENCES_PATH + "/list")
    public ResponseEntity<?> getAllCompanyLicencesId(@RequestBody Long id) {
        try {
            List<UUID> result = licenceFacade.getAllCompanyLicencesId(id);
            return ResponseEntity.ok(result);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Проверка корректности и актуальности лицензии
     *
     * @param licenceString - лицензия
     * @return 200 OK, если лицензия корректна и актуальна,
     * иначе код 418 с типом ошибки в теле ответа: LICENSE_NOT_EXIST, LICENSE_EXPIRED
     */
    @PostMapping(LICENCES_PATH + "/check")
    public ResponseEntity<String> checkLicence(@RequestBody String licenceString) {
        try {
            if (licenceFacade.isLicenceCorrect(licenceString)) {
                return ResponseEntity.ok("OK");
            } else {
                return ResponseEntity.status(418).body(LICENCE_NOT_EXIST);
            }
        } catch (LicenceException | DataNotFoundException e) {
            return ResponseEntity.status(418).body(e.getMessage());
        }
    }
}
