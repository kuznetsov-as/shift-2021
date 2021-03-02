package ftc.shift.sample.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
