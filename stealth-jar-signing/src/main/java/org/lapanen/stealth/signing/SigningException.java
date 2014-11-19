package org.lapanen.stealth.signing;

public class SigningException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 4228180946838246757L;

    public SigningException() {
        super();
    }

    public SigningException(String message) {
        super(message);
    }

    public SigningException(Throwable cause) {
        super(cause);
    }

    public SigningException(String message, Throwable cause) {
        super(message, cause);
    }

}
