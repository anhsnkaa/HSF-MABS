package org.mabs.exception;

public class DuplicateEmailException extends RuntimeException {
    private final String email;

    public DuplicateEmailException(String email) {
        super("Email đã tồn tại: " + email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
