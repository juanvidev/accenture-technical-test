package co.com.accenture.api.exception;


import co.com.accenture.model.exception.CustomBaseException;

import java.util.List;

public class ConstraintViolationException extends CustomBaseException {
    public ConstraintViolationException( List<String> violations ) {
        super(
            400,
            "Validation failed",
            "CONSTRAINT_VIOLATION",
            violations
        );
    }
}
