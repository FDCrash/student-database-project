package com.netcracker.denisik.exteption;

public class ResourceAlreadyExistException extends RuntimeException {
    public ResourceAlreadyExistException() {
        super();
    }

    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
