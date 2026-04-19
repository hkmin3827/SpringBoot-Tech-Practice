package springallinone.practice.webclient.exception;

public class ExternalServerException extends RuntimeException {
    public ExternalServerException(String body) {
        super("5xx Server Error: " + body);
    }
}
