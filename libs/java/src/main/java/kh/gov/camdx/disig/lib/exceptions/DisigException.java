package kh.gov.camdx.disig.lib.exceptions;

public class DisigException extends Exception {
    protected final Integer error;

    public DisigException(Integer error) {
        this.error = error;
    }

    public DisigException(String message, Integer error) {
        super(message);
        this.error = error;
    }

    public DisigException(String message, Throwable cause, Integer error) {
        super(message, cause);
        this.error = error;
    }
}
