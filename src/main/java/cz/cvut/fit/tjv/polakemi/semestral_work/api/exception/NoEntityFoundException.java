package cz.cvut.fit.tjv.polakemi.semestral_work.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoEntityFoundException extends ResponseStatusException {
    public NoEntityFoundException() {
        super(HttpStatus.NOT_FOUND, "No entity found");
    }
}
