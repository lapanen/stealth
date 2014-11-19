package org.lapanen.stealth;

public class StealthException extends RuntimeException {
    private static final long serialVersionUID = 8124434878737950740L;

    public StealthException() {
        super();
    }

    public StealthException(String message) {
        super(message);
    }

    public StealthException(Throwable cause) {
        super(cause);
    }

    public StealthException(String message, Throwable cause) {
        super(message, cause);
    }
}