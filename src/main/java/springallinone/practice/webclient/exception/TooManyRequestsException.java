package springallinone.practice.webclient.exception;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String body) {
        super("429 Too Many Requests: " + body);
    }
}
