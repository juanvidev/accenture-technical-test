package co.com.accenture.model.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CustomBaseException extends RuntimeException {
    private int statusCode;
    private Boolean success = false;
    private String message;
    private LocalDateTime timestamp;
    private String errorCode;
    private List<String> errors;

    public CustomBaseException(int statusCode, String message, String errorCode, List<String> errors) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.errorCode = errorCode;
        this.errors = errors;
    }

    public CustomBaseException(int statusCode, String message, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.errorCode = errorCode;
    }
}

