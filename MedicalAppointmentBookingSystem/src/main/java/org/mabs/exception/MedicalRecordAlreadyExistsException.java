package org.mabs.exception;

public class MedicalRecordAlreadyExistsException extends RuntimeException {
    public MedicalRecordAlreadyExistsException(String message) {
        super(message);
    }
}
