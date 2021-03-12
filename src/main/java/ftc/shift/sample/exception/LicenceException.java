package ftc.shift.sample.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LicenceException extends Exception {
    public LicenceException(String message) {
        super(message);
    }
}
