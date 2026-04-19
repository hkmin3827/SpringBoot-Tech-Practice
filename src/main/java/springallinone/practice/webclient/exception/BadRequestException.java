package springallinone.practice.webclient.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String body) {
        super("400 Bad Request: " + body);
    }
}
