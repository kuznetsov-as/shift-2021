package ftc.shift.sample.controller;

import ftc.shift.sample.exception.LicenceCorrectnessException;
import ftc.shift.sample.exception.LicenceGeneratorException;
import ftc.shift.sample.service.LicenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class LicencesController {
    private static final String LICENCES_PATH = "/licences";
    private final LicenceService service;

    @Autowired
    public LicencesController(LicenceService service) {
        this.service = service;
    }

    /**
     * Добавление новой лицензии
     *
     * @param id - id пользователя или компании, для новой лицензии
     * @return Сохранённую лицензию
     */
    @PostMapping(LICENCES_PATH + "/new")
    public ResponseEntity<String> createLicence(@RequestBody Long id) {
        System.out.println("Пришли   /new");
        try {
            String result = service.createLicence(id);
            return ResponseEntity.ok(result);
        } catch (LicenceGeneratorException exception) {
            return ResponseEntity.status(418).body("Не удалось создать лицензию");
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
        String result = service.getLicence(licenceId, id);
        return ResponseEntity.ok(result);
    }

    /**
     * Получение всех лицензий компании
     *
     * @param id - id компании
     * @return Список всех ID лицензий для полученного id компании
     */
    @PostMapping(LICENCES_PATH + "/list")
    public ResponseEntity<List<UUID>> getAllCompanyLicencesId(@RequestBody Long id) {
        List<UUID> result = service.getAllCompanyLicencesId(id);
        return ResponseEntity.ok(result);
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
            if (service.isLicenceCorrect(licenceString)) {
                return ResponseEntity.ok("Лицензия корректна и актуальна");
            } else {
                throw new LicenceCorrectnessException("Лицензия некорректна");
            }
        } catch (LicenceCorrectnessException e) {
            return ResponseEntity.status(418).body(e.getMessage());
        }
    }
}
