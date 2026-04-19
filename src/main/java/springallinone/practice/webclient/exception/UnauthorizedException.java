package springallinone.practice.webclient.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String body) {
        super("401 Unauthorized: " + body);
    }
}
