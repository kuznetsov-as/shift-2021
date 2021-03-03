package ftc.shift.sample.exception;

public class LicenceGeneratorException extends Exception {
    public LicenceGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public LicenceGeneratorException(Throwable cause) {
        super(cause);
    }
}
