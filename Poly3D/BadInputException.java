package Poly3D;

import java.lang.Exception;

public class BadInputException extends Exception {

    public BadInputException() {}

    public BadInputException(String message) {
        super(message);
    }
}
