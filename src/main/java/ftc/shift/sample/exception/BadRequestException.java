package ftc.shift.sample.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}