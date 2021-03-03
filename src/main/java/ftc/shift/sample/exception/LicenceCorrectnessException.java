package ftc.shift.sample.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LicenceCorrectnessException extends Exception {
    public LicenceCorrectnessException(String message) {
        super(message);
    }
}
