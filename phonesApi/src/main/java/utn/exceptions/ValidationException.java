package utn.exceptions;

public class ValidationException extends Exception {
    String msg;
    public ValidationException() {
        this.msg = "error al insertar";
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
