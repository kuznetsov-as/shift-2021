package ftc.shift.sample.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DataNotFoundException extends Exception {
    public DataNotFoundException(String message) {
        super(message);
    }
}
