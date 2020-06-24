package utn.exceptions;

public class InvalidLoginException extends Throwable {
    String message;

    public InvalidLoginException(Throwable cause, String message) {
        super(cause);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
