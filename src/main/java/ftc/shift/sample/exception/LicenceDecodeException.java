package ftc.shift.sample.exception;

public class LicenceDecodeException extends Exception {
    public LicenceDecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LicenceDecodeException(String message) {
        super(message);
    }

    public LicenceDecodeException(Throwable cause) {
        super(cause);
    }
}
