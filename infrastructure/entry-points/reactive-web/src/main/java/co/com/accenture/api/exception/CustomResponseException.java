package co.com.accenture.api.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class CustomResponseException {
    private Boolean success;
    private String message;
    private LocalDateTime timestamp;
    private String errorCode;
    private List<String> errors;

}
