package springallinone.practice.webclient.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String body) {
        super("404 Not Found: " + body);
    }
}
