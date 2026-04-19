package springallinone.practice.webclient.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String body) {
        super("403 Forbidden: " + body);
    }
}
