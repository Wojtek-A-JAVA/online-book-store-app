package book.store.exception;

public class QuantityTooLowException extends RuntimeException {
    public QuantityTooLowException(String message) {
        super(message);
    }
}
