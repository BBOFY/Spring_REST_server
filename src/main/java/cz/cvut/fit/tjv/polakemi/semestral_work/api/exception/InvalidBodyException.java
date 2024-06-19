package cz.cvut.fit.tjv.polakemi.semestral_work.api.exception;

public class InvalidBodyException extends RuntimeException {
    public InvalidBodyException() {
        super("Invalid request body");
    }
}
